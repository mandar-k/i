package client.modals


import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.OnUnmount
import japgolly.scalajs.react.vdom.prefix_<^._
import client.components.Bootstrap._
import client.components.Icon
import client.components.Icon._
import client.components._
import client.css.{DashBoardCSS, HeaderCSS, ProjectCSS}
import client.modules.AppModule
import client.services.{CoreApi, LGCircuit}
import japgolly.scalajs.react
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import scala.language.reflectiveCalls
import org.querki.jquery._
import com.livelygig.product.shared.models.{ProjectPostContent, ProjectsPost}
import client.utils.{AppUtils, ConnectionsUtils, ContentUtils}
import diode.AnyAction._
import org.querki.facades.bootstrap.datepicker._
import scala.scalajs.js

object NewProject {


  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(buttonName: String, addStyles: Seq[StyleA] = Seq(), addIcons: Icon, title: String)

  case class State(showNewProjectForm: Boolean = false)

  abstract class RxObserver[BS <: BackendScope[_, _]](scope: BS) extends OnUnmount {
  }

  class Backend(t: BackendScope[Props, State]) extends RxObserver(t) {
    def mounted(props: Props): Callback = {
      t.modState(s => s.copy(showNewProjectForm = true))
    }

    def addProjectForm(): Callback = {
      t.modState(s => s.copy(showNewProjectForm = false))
    }
    def addNewProjectForm(/*postProject: Boolean*/): Callback = {
      t.modState(s => s.copy(showNewProjectForm = true))
//      if (postProject) {
//        t.modState(s => s.copy(showNewProjectForm = false))
//      } else {
//        t.modState(s => s.copy(showNewProjectForm = true))
//      }
    }
  }

  val component = ReactComponentB[Props]("AddNewAgent")
    .initialState(State())
    .backend(new Backend(_))
    .renderPS(($, P, S) => {
      val B = $.backend
      <.div(/*ProjectCSS.Style.displayInitialbtn*/)(
        Button(Button.Props(B.addNewProjectForm(), CommonStyle.default, P.addStyles, P.addIcons, P.title, className = "profile-action-buttons"), P.buttonName),
        if (S.showNewProjectForm) NewProjectForm(NewProjectForm.Props(B.addProjectForm))
        else
          Seq.empty[ReactElement]
      )
    })
    .configure(OnUnmount.install)
    .build

  def apply(props: Props) = component(props)
}

object NewProjectForm {
  // shorthand for styles
  @inline private def bss = GlobalStyles.bootstrapStyles

  val jobStartDate: js.Object = "#jobStartDate"
  val jobEndDate: js.Object = "#jobEndDate"

  case class Props(submitHandler: (/*Boolean*/) => Callback)

  case class State(projectPost: ProjectPostContent, postProject: Boolean = false, selectizeInputId: String = "postNewJobSelectizeInput")

  case class Backend(t: BackendScope[Props, State]) {
    def hide = Callback {
      // instruct Bootstrap to hide the modal
      $(t.getDOMNode()).modal("hide")
    }

    def hideModal(): Unit = {
      $(t.getDOMNode()).modal("hide")
    }

    def submitForm(e: ReactEventI): react.Callback = {
      e.preventDefault()
      val state = t.state.runNow()
      val req = AppUtils.getPostData(state.projectPost, ConnectionsSelectize.getConnectionsFromSelectizeInput(state.selectizeInputId) ++ Seq(ConnectionsUtils.getSelfConnnection(AppModule.PROJECTS_VIEW)), Nil, AppModule.PROJECTS_VIEW)
      ContentUtils.postContent(req)
      t.modState(s => s.copy(postProject = true))
    }

    def formClosed(state: State, props: Props): Callback = {
      props.submitHandler(/*state.postProject*/)
    }

    def updateName(event: ReactEventI): react.Callback = {
      val value = event.target.value
      t.modState(s => s.copy(projectPost = s.projectPost.copy(name = value)))
    }

    def updateContractType(event: ReactEventI): react.Callback = {
      val value = event.target.innerHTML
      t.modState(s => s.copy(projectPost = s.projectPost.copy(contractType = value)))
    }

    def updateWorkLocation(event: ReactEventI): react.Callback = {
      val value = event.target.innerHTML
      t.modState(s => s.copy(projectPost = s.projectPost.copy(workLocation = value)))
    }

    def updateBudget(event: ReactEventI): react.Callback = {
      val value = event.target.value
      t.modState(s => s.copy(projectPost = s.projectPost.copy(budget = value)))
    }

    def updateDescription(event: ReactEventI): react.Callback = {
      val value = event.target.value
      t.modState(s => s.copy(projectPost = s.projectPost.copy(description = value)))
    }

    def updateMessage(event: ReactEventI): react.Callback = {
      val value = event.target.value
      t.modState(s => s.copy(projectPost = s.projectPost.copy(message = value)))
    }

    def updateSkillNeeded(event: ReactEventI): react.Callback = {
      val value = event.target.value
      t.modState(s => s.copy(projectPost = s.projectPost.copy(skillNeeded = value)))
    }

    def updateAllowForwarding(event: ReactEventI): react.Callback = {
      val value = event.target.checked
      t.modState(s => s.copy(projectPost = s.projectPost.copy(allowForwarding = value)))
    }

    def fromSelecize(): Callback = Callback {}

    def initializeDatepicker(): Unit = {
      val baseOpts = BootstrapDatepickerOptions.
        autoclose(true).
        todayHighlight(true).
        todayBtnLinked().
        disableTouchKeyboard(true).
        orientation(Orientation.Bottom)
      // Iff this Date is Optional, show the Clear button:
      val opts =
      if (true)
        baseOpts.clearBtn(true)
      else
        baseOpts
      $(jobStartDate).datepicker(baseOpts)
      $(jobEndDate).datepicker(baseOpts)
      $(jobStartDate).on("changeDate", { rawEvt:JQueryEventObject => println(s"start date =${$(jobStartDate).value().toString }")
        t.modState(s => s.copy(projectPost = s.projectPost.copy(startDate = $(jobStartDate).value().toString ))).runNow()
      })
      $(jobEndDate).on("changeDate", { rawEvt:JQueryEventObject => println(s"end date =${$(jobEndDate).value().toString}")
        t.modState(s => s.copy(projectPost = s.projectPost.copy(endDate = $(jobEndDate).value().toString))).runNow()
      })

    }

    // scalastyle:off
    def render(s: State, p: Props) = {
      val connectionsProxy = LGCircuit.connect(_.connections)
      val headerText = "New Job"
      val model = s.projectPost
      Modal(
        Modal.Props(
          // header contains a cancel button (X)
          header = hide => <.span(<.button(^.tpe := "button", bss.close, ^.onClick --> hide, Icon.close), <.div(DashBoardCSS.Style.modalHeaderText)(headerText)),
          // this is called after the modal has been hidden (animation is completed)
          closed = () => formClosed(s, p)
        ),
        <.form(^.onSubmit ==> submitForm )(
          <.div(^.className := "row")(
            <.div(^.className := "col-md-6 col-sm-6 col-xs-12")(
              <.div(^.className := "row")(
                <.div(^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.slctInputWidthLabel)(
                  <.label(^.`for` := "Project Name", "Project Name")
                ),
                <.div(DashBoardCSS.Style.scltInputModalLeftContainerMargin)(
                  <.input(^.tpe := "text", bss.formControl, DashBoardCSS.Style.inputModalMargin, ^.value := model.name, ^.required := true, ^.onChange ==> updateName)
                )
              ),
              <.div(^.className := "row")(
                <.div(^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.slctInputWidthLabel)(
                  <.label(^.`for` := "Start Date", "Start Date")
                ),
                <.div(DashBoardCSS.Style.scltInputModalLeftContainerMargin)(
                  <.input(^.tpe := "text", bss.formControl, DashBoardCSS.Style.inputModalMargin, ^.defaultValue:=model.startDate,
                    "data-provide".reactAttr := "datepicker", ^.id := "jobStartDate", ^.placeholder := " Start date",  ^.required := true)
                )
              ),
              <.div(^.className := "row")(
                <.div(^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.slctInputWidthLabel)(
                  <.label(^.`for` := "Budget", "Budget")
                ),
                <.div(DashBoardCSS.Style.scltInputModalLeftContainerMargin)(
                  <.input(^.tpe := "text", bss.formControl, DashBoardCSS.Style.inputModalMargin, ^.value := model.budget,
                    ^.required := true, ^.onChange ==> updateBudget)
                )
              ),
              <.div(^.className := "row")(
                <.div(^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.slctInputWidthLabel)(
                  <.label(^.`for` := "Currency", "Currency")
                ),
                <.div(DashBoardCSS.Style.scltInputModalLeftContainerMargin)(
                  //                <.input(^.tpe := "text", bss.formControl, DashBoardCSS.Style.inputModalMargin ,^.id := "Name")
                  // <.input(^.`type` := "radio")(" client")
                  <.div(^.className := "btn-group")(
                    <.button(ProjectCSS.Style.projectdropdownbtn, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown")("Select One  ")(
                      <.span(^.className := "caret")
                    ),
                    <.ul(^.className := "dropdown-menu")(
                      <.li()(<.a()("Dollar ")),
                      <.li()(<.a()("INR")),
                      <.li()(<.a()("Pound"))
                    )
                    // )
                  )
                )
              )
            ), //col-md-8
            <.div(^.className := "col-md-6 col-sm-6 col-xs-12")(
              <.div(^.className := "row")(
                <.div(^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.slctInputWidthLabel)(
                  <.label(^.`for` := "Contact Type", "Contact Type")
                ),
                <.div(DashBoardCSS.Style.scltInputModalLeftContainerMargin)(
                  //                <.input(^.tpe := "text", bss.formControl, DashBoardCSS.Style.inputModalMargin ,^.id := "Name")
                  // <.input(^.`type` := "radio")(" client")
                  <.div(^.className := "btn-group")(
                    <.button(ProjectCSS.Style.projectdropdownbtn, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown",^.value:=model.contractType)
                    (if (model.contractType != "") model.contractType else "Contract Type")  (
                      <.span(^.className := "caret")
                    ),
                    <.ul(^.className := "dropdown-menu")(
                      <.li()(<.a(^.onClick ==> updateContractType)("Item 1")),
                      <.li()(<.a(^.onClick ==> updateContractType)("Item 2")),
                      <.li()(<.a(^.onClick ==> updateContractType)("Item 3"))
                    )
                  )
                )
              ),

              <.div(^.className := "row")(
                <.div(^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.slctInputWidthLabel)(
                  <.label(^.`for` := "End Date", "End Date")
                ),
                <.div(DashBoardCSS.Style.scltInputModalLeftContainerMargin)(
                  <.input(^.tpe := "text", bss.formControl, DashBoardCSS.Style.inputModalMargin,^.defaultValue:=model.endDate,
                    "data-provide".reactAttr := "datepicker", ^.id := "jobEndDate", ^.placeholder := " End date",^.required := true)
                )
              ),


              <.div(^.className := "row")(
                <.div(^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.slctInputWidthLabel)(
                  <.label(^.`for` := "Work Location", "Work Location")
                ),
                <.div(DashBoardCSS.Style.scltInputModalLeftContainerMargin)(
                  //                <.input(^.tpe := "text", bss.formControl, DashBoardCSS.Style.inputModalMargin ,^.id := "Name")
                  // <.input(^.`type` := "radio")(" client")
                  <.div(^.className := "btn-group")(
                    <.button(ProjectCSS.Style.projectdropdownbtn, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown", ^.value := model.workLocation)
                    (if (model.workLocation != "") model.workLocation else "Work Location ")(
                      <.span(^.className := "caret")
                    ),
                    <.ul(^.className := "dropdown-menu")(
                      <.li()(<.a(^.onClick ==> updateWorkLocation)("Pune")),
                      <.li()(<.a(^.onClick ==> updateWorkLocation)("Bangalore")),
                      <.li()(<.a(^.onClick ==> updateWorkLocation)("U.S.A"))
                    )
                  )
                )
              )
            ) //col-md-4
          ), //main row
          <.div(^.className := "row", DashBoardCSS.Style.MarginLeftchkproduct)(
            <.div(DashBoardCSS.Style.marginTop10px)(),
            <.div()(
              <.input(^.`type` := "textarea", ProjectCSS.Style.textareaWidth, ^.placeholder := "Describe the project:", ^.lineHeight := 4, ^.value := model.description, ^.onChange ==> updateDescription ,^.required:="true")
            ),
            <.div(DashBoardCSS.Style.marginTop10px)(
              <.input(^.`type` := "textarea", ProjectCSS.Style.textareaWidth, ^.placeholder := "Message for selected members:", ^.lineHeight := 4, ^.value := model.message, ^.onChange ==> updateMessage ,^.required:="true")
            ),
            <.div(DashBoardCSS.Style.marginTop10px)(
              <.input(^.`type` := "textarea", ProjectCSS.Style.textareaWidth, ^.placeholder := "Skill needed:", ^.lineHeight := 4, ^.value := model.skillNeeded, ^.onChange ==> updateSkillNeeded ,^.required:="true")
            ),
            <.div(DashBoardCSS.Style.marginTop10px, ^.id := s.selectizeInputId)(
              connectionsProxy(connectionsProxy => ConnectionsSelectize(ConnectionsSelectize.Props(connectionsProxy, s.selectizeInputId, fromSelecize)))
            )
          ),
          <.div()(
            <.div(DashBoardCSS.Style.modalHeaderPadding, DashBoardCSS.Style.footTextAlign, DashBoardCSS.Style.marginTop10px)(
              <.div(
                <.input(^.`type` := "checkbox", ^.checked := model.allowForwarding, ^.onChange ==> updateAllowForwarding),
                <.span(^.fontWeight := "bold")(" Allow forwarding")
              )

            ),
            <.div(DashBoardCSS.Style.modalHeaderPadding, ^.className := "text-right")(
              //<.button(^.tpe := "submit",^.className:="btn btn-default","Submit"),
              <.button(^.tpe := "button", ^.className := "btn", DashBoardCSS.Style.btnDefault, DashBoardCSS.Style.marginLeftCloseBtn, ^.onClick --> hide, "Save as Draft"),
              <.button(^.tpe := "submit", ^.className := "btn", DashBoardCSS.Style.btnDefault, DashBoardCSS.Style.marginLeftCloseBtn, "Submit" ),
              <.button(^.tpe := "button", ^.className := "btn", DashBoardCSS.Style.btnDefault, DashBoardCSS.Style.marginLeftCloseBtn, ^.onClick --> hide, "Cancel")
            )
          ),
          <.div(bss.modal.footer, DashBoardCSS.Style.marginTop10px, DashBoardCSS.Style.marginLeftRight)()
        )
      )
    }
  }
  private val component = ReactComponentB[Props]("PostAProjectForm")
    .initialState_P(p => State(new ProjectPostContent("", "", "", "", "", "", "", "", false, 0, "")))
    .renderBackend[Backend]
    .componentDidMount(scope => Callback {
      scope.backend.initializeDatepicker()
    })
    .componentDidUpdate(scope => Callback {
      if (scope.currentState.postProject) {
        scope.$.backend.hideModal()
      }
    })
    .build

  def apply(props: Props) = component(props)
}
