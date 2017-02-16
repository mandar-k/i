package client.modules

import client.rootmodel.ProjectsRootModel
import client.css._
import com.livelygig.product.shared.models.ProjectsPost
import client.components.Bootstrap._
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import client.components._
import client.css.{DashBoardCSS, HeaderCSS}
import client.modals._
import client.services.LGCircuit
import japgolly.scalajs.react
import org.querki.jquery.{$, _}
import org.widok.moment.Moment
import scala.scalajs.js
import scalacss.ScalaCssReact._
import scala.language.existentials

object ProjectResults {

  case class Props(proxy: ModelProxy[Pot[ProjectsRootModel]])

  case class State(showErrorModal: Boolean = false ,menuAction : String = "" ,sortMenuBy  : String = "")

  val getServerError = LGCircuit.zoom(_.appRootModel).value

  class Backend(t: BackendScope[Props, State]) {
    def mounted(props: Props): react.Callback = Callback {
      //      log.debug("project view mounted")
      /*if (props.proxy().isEmpty) {
        ContentModelHandler.subsForContentAndBeginSessionPing(AppModule.PROJECTS_VIEW)
      }*/
      val addTooltip: js.Object = ".DashBoardCSS_Style-btn"
      $(addTooltip).tooltip(PopoverOptions.html(true))

    }

    def dropDownSelectAction(event: ReactEventI): Callback = {
      val value = event.target.innerHTML
      t.modState(s => s.copy(menuAction = value))
    }

    def dropDownMenuSorting(event: ReactEventI): Callback = {
      val valueSort = event.target.innerHTML
      t.modState(s => s.copy(sortMenuBy = valueSort))
    }


    def serverError(showErrorModal: Boolean = false): Callback = {
      if (showErrorModal)
        t.modState(s => s.copy(showErrorModal = false))
      else
        t.modState(s => s.copy(showErrorModal = true))
    }

    def render(P: Props, S: State) = {

      <.div(^.id := "rsltScrollContainer", DashBoardCSS.Style.rsltContainer)(
        <.div(DashBoardCSS.Style.gigActionsContainer, ^.className := "row")(
         <.div(^.className := "col-md-4 col-sm-4 col-xs-8")(
            <.input(^.`type` := "checkbox", DashBoardCSS.Style.rsltCheckboxStyle),
            <.div(^.display := "inline-block")(
              <.div(DashBoardCSS.Style.displayInlineText, ^.className := "dropdown")(
                <.button(DashBoardCSS.Style.gigMatchButton, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown")
                (if(S.menuAction.equals("")) "Select Bulk Action " else  S.menuAction )(
                  <.span(^.className := "caret", DashBoardCSS.Style.rsltCaretStyle)
                ),
                <.ul(^.className := "dropdown-menu")(
                  <.li()(<.a(^.onClick ==> dropDownSelectAction)("Hide")),
                  <.li()(<.a(^.onClick ==> dropDownSelectAction)("Favorite")),
                  <.li()(<.a(^.onClick ==> dropDownSelectAction)("Unhide")),
                  <.li()(<.a(^.onClick ==> dropDownSelectAction)("Unfavorite"))
                )
              ), <.div(PresetsCSS.Style.modalBtn)(
                NewProject(NewProject.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.clipboard, "Create New Job")),
                <.div(PresetsCSS.Style.overlay)(
                  Icon.plus
                )
              )
            )
          ),
          <.div(^.className := "col-md-2 col-sm-2 col-xs-4")(
            <.div(DashBoardCSS.Style.displayInlineText, DashBoardCSS.Style.rsltCountHolderDiv, DashBoardCSS.Style.marginResults)("2,352 Results")
          ),
          <.div(^.className := "col-md-6 col-sm-6 col-xs-12")(
            <.div(^.display := "inline-block",DashBoardCSS.Style.rsltSortingDropdown)(
              <.div(DashBoardCSS.Style.displayInlineText, ^.className := "dropdown")(
                <.button(DashBoardCSS.Style.gigMatchButton, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown")
                (if(S.sortMenuBy.equals("")) "By Date" else  S.sortMenuBy )(
                  <.span(^.className := "caret", DashBoardCSS.Style.rsltCaretStyle)
                ),
                <.ul(^.className := "dropdown-menu")(
                  <.li()(<.a(^.onClick ==> dropDownMenuSorting)("By Date")),
                  <.li()(<.a(^.onClick ==> dropDownMenuSorting)("By Experience")),
                  <.li()(<.a(^.onClick ==> dropDownMenuSorting)("By Reputation")),
                  <.li()(<.a(^.onClick ==> dropDownMenuSorting)("By Rate")),
                  <.li()(<.a(^.onClick ==> dropDownMenuSorting)("By Projects Completed"))
                )
              ),
              <.div(DashBoardCSS.Style.displayInlineText, ^.className := "dropdown")(
                <.button(DashBoardCSS.Style.gigMatchButton, DashBoardCSS.Style.padding0px, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown")("Newest ")(
                  <.span(Icon.longArrowDown)
                )
              )
            ),
            <.div(^.className := "pull-right")(
              <.button(DashBoardCSS.Style.btn, "data-toggle".reactAttr := "tooltip", "title".reactAttr := "View Summary", "data-placement".reactAttr := "bottom")(<.span(Icon.minus)),
              <.button(DashBoardCSS.Style.customEqualIconButton,DashBoardCSS.Style.btn, "data-toggle".reactAttr := "tooltip", "title".reactAttr := "View Brief", "data-placement".reactAttr := "bottom")(<.span("=",DashBoardCSS.Style.equalsIcon)),
              <.button(DashBoardCSS.Style.btn, "data-toggle".reactAttr := "tooltip", "title".reactAttr := "View Full Posts", "data-placement".reactAttr := "bottom")(<.span(Icon.bars))
            )
          )
        ), //col-12
        <.div(^.id := "resultsContainer")(
          P.proxy().render(jobPostsRootModel =>
            ProjectsList(jobPostsRootModel.projectsModelList)),
          P.proxy().renderFailed(ex => <.div()(
            //<.span(Icon.warning), " Error loading")
            if (!getServerError.isServerError) {
              ServerErrorModal(ServerErrorModal.Props(serverError, "Server offline"))
            }
            else
              <.div()
          )),
          if (P.proxy().isEmpty) {
            <.div()(
              <.img(^.src := "./assets/images/processing.gif", DashBoardCSS.Style.imgc)
            )
          } else {
            <.div()
          }
        ) //gigConversation
      )
    }
  }

  // create the React component for Dashboard
  val component = ReactComponentB[Props]("Projects")
    .initialState_P(p => State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[ProjectsRootModel]]) = component(Props(proxy))
}

object ProjectsList {

  case class Props(projects: Seq[ProjectsPost])

  case class Backend(t: BackendScope[Props, _]) {
    def mounted(props: Props): Callback = Callback {
      val msgTime: js.Object = ".msgTime"
      $(msgTime).tooltip(PopoverOptions.html(true))
    }

    def render(p: Props) = {
      def renderJobPosts(project: ProjectsPost) = {
        <.li(^.className := "media", DashBoardCSS.Style.profileDescription, DashBoardCSS.Style.rsltpaddingTop10p)(
          <.input(^.`type` := "checkbox", DashBoardCSS.Style.rsltCheckboxStyle),
          <.span(^.className := "checkbox-lbl"),
          <.div(DashBoardCSS.Style.profileNameHolder)(
            project.postContent.name,
            <.div(DashBoardCSS.Style.displayInlineText)("  Posted by: @LivelyGig  "),
            <.div(DashBoardCSS.Style.displayInlineText)("  Posted: "),
            <.div(DashBoardCSS.Style.displayInlineText, ^.className := "msgTime", "data-toggle".reactAttr := "tooltip", ^.title := project.created, "data-placement".reactAttr := "right")(Moment(project.created).toLocaleString)
          ),
          <.div(^.className := "media-body", ^.paddingLeft := "28px")(
            <.div(s"Job Type: ${project.postContent.contractType}"),
            <.div(project.postContent.description),
            <.div(/*^.className := "col-md-4 col-sm-4",*/DashBoardCSS.Style.postResultDescriptionMargin)(
              <.div( s"skill Needed : ${project.postContent.skillNeeded}" ),

                 s"start date : ${project.postContent.startDate}",
                 <.br(),
                 s"end date : ${project.postContent.endDate}",
                 <.br(),
                 s"Message : ${ project.postContent.message}",
                 <.br(),
                   s"Budget : ${project.postContent.budget}",
                 <.br(),
                   s"Work Location: ${project.postContent.workLocation}",
                   <.br(),
              <.div( "Recommended By: @Tom")
              // project.pageOfPosts.skills.toString()
            ),
            <.div()(
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Hide", Icon.remove),
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Favorite", Icon.star),
              RecommendationJobs(RecommendationJobs.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.thumbsOUp, "Recommend Job")),
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Find Matching Talent", Icon.users),
              WorkContractModal(WorkContractModal.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.pencilSquareO, "Apply")),
              NewMessage(NewMessage.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.envelope, "Message"))
            )
          ) //media-body
        ) //li
      }
      <.div(DashBoardCSS.Style.rsltSectionContainer)(
        <.ul(^.className := "media-list")(p.projects map renderJobPosts)
      )
    }
  }

  val ProjectsList = ReactComponentB[Props]("ProjectList")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(jobPosts: Seq[ProjectsPost]) = ProjectsList(Props(jobPosts))
}
