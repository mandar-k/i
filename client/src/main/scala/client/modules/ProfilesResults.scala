package client.modules

import client.components.Bootstrap._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react._
import client.components.Icon
import client.css.{DashBoardCSS, HeaderCSS, PresetsCSS}
import client.modals.{NewMessage, NewProfile, NewRecommendation, ServerErrorModal}
import client.rootmodel.ProfilesRootModel
import client.services.LGCircuit
import com.livelygig.product.shared.models.ProfilesPost
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react
import org.querki.jquery._
import scala.scalajs.js
import scalacss.ScalaCssReact._
import org.querki.jquery.$

object ProfilesResults {

  case class Props(proxy: ModelProxy[Pot[ProfilesRootModel]])

  case class State(showErrorModal: Boolean = false ,menuAction : String = "" ,sortMenuBy  : String = "")

  val getServerError = LGCircuit.zoom(_.appRootModel).value

  class Backend(t: BackendScope[Props, State]) {
    def mounted(props: Props): react.Callback = Callback {
      //      log.debug("profiles view mounted")
      /*if (props.proxy().isEmpty) {
        ContentModelHandler.subsForContentAndBeginSessionPing(AppModule.PROFILES_VIEW)
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
                NewProfile(NewProfile.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.user, "Create Profile")),
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
            <.div(^.display := "inline-block")(
              <.div(DashBoardCSS.Style.displayInlineText, ^.className := "dropdown")(
                <.button(DashBoardCSS.Style.gigMatchButton, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown")
                (if(S.sortMenuBy.equals("")) "By Date" else  S.sortMenuBy )(
                  <.span(^.className := "caret", DashBoardCSS.Style.rsltCaretStyle)
                ),
                <.ul(^.className := "dropdown-menu",DashBoardCSS.Style.rsltSortingDropdown)(
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
        <.div(^.className := "container-fluid", ^.id := "resultsContainer")(
          P.proxy().render(profilesPostsRootModel =>
            ProfilesList(profilesPostsRootModel.profilesList)),
          P.proxy().renderFailed(ex => <.div()(

            // <.span(Icon.warning), " Error loading")
            if (!getServerError.isServerError) {
              ServerErrorModal(ServerErrorModal.Props(serverError, "Server offline"))
            }
            else
              <.div())

          ),
          if (P.proxy().isEmpty) {
            <.div()(
              <.img(^.src := "./assets/images/processing.gif", DashBoardCSS.Style.imgc)
            )
          } else {
            <.div()
          }
        )
      )
    }
  }

  private val component = ReactComponentB[Props]("Talent")
    .initialState_P(p => State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[ProfilesRootModel]]) = component(Props(proxy))

}

object ProfilesList {

  case class Props(profiles: Seq[ProfilesPost])

  private val ProfilesList = ReactComponentB[Props]("ProjectList")
    .render_P(p => {
      def renderProfilePosts(profile: ProfilesPost) = {
        <.li(^.className := "media profile-description", DashBoardCSS.Style.rsltpaddingTop10p)(
          <.input(^.`type` := "checkbox", DashBoardCSS.Style.rsltCheckboxStyle),
          <.span(^.className := "checkbox-lbl"),
          <.div(DashBoardCSS.Style.profileNameHolder)(s"${profile.postContent.talentProfile.name}, Videographer"),
          <.div(^.className := "col-md-12")(
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)("Experience: 8 years"),
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)("Projects Completed: 24"),
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)("Availability: Negotiable"),
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)(
              ("Recommended By: "),
              <.a()("@Britta"),
              (" for Project: "),
              <.a()("9347383"),
              (" Need Videographer...")
            )
          ),
          <.div(^.className := "media-left", ^.paddingLeft := "24px")(
            <.a(^.href := "https://www.youtube.com/embed/0oHhD3Bk9Uc?rel=0", ^.target := "new", (
              <.img(DashBoardCSS.Style.profileImg, ^.src := "./assets/images/profile-img2.jpg")
              ))
          ),
          <.div(^.className := "media-body")(
            "lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
            <.div(^.className := "col-md-12 col-sm-12 ")(
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Hide", Icon.remove),
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Favorite", Icon.star),
              NewRecommendation(NewRecommendation.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.thumbsOUp, "Recommend")),
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Hire Me", Icon.rocket),
              NewMessage(NewMessage.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.envelope, "Message"))
            )
          )
        )
      }
      <.div(^.className := "rsltSectionContainer")(
        <.ul(^.className := "media-list")(p.profiles map renderProfilePosts)
      )
    })
    .build

  def apply(profilesPosts: Seq[ProfilesPost]) = ProfilesList(Props(profilesPosts))
}
