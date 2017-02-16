package client.modules

import client.components.Bootstrap._
import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}
import client.rootmodel.ConnectionsRootModel
import com.livelygig.product.shared.models.{ConnectionsModel}
import client.components.Icon
import client.css.{DashBoardCSS, HeaderCSS, PresetsCSS}
import client.modals.{NewConnection, NewMessage, NewRecommendation}
import diode.react.ModelProxy
import japgolly.scalajs.react
import client.modals.{NewMessage, NewRecommendation}
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import client.components._
import scala.scalajs.js
import scalacss.ScalaCssReact._
import org.querki.jquery.$

import scala.scalajs.js

object ConnectionsResults {

  case class Props(proxy: ModelProxy[ConnectionsRootModel] )

  case class State(selectedItem: Option[ConnectionsModel] = None ,menuAction : String = "" ,sortMenuBy  : String = "" )

   class Backend(t: BackendScope[Props, State]) {
    def mounted(props: Props): Callback = Callback {
      //      log.debug("connection view mounted")
      //      Callback.when(props.proxy().isEmpty)(props.proxy.dispatch(RefreshConnections()))
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
                NewConnection(NewConnection.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.connectdevelop, "Create New Connection")),
                <.div(PresetsCSS.Style.overlay, ^.top := "0.1em")(
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
        ConnectionList(P.proxy().connectionsResponse))
      ) //mainContainer
    }
  }

  private val component = ReactComponentB[Props]("Connection")
    .initialState_P(p => State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build


  def apply(proxy: ModelProxy[ConnectionsRootModel]) = component(Props(proxy))
}

object ConnectionList {

  case class ConnectionListProps(connections: Seq[ConnectionsModel])

  val ConnectionList = ReactComponentB[ConnectionListProps]("ConnectionList")
    .render_P(p => {
      def renderConnections(connection: ConnectionsModel) = {
        // <.li(^.className := "media  profile-description", DashBoardCSS.Style.rsltpaddingTop10p)(
        <.li(^.className := "media", DashBoardCSS.Style.profileDescription, DashBoardCSS.Style.rsltpaddingTop10p)(
          <.input(^.`type` := "checkbox", DashBoardCSS.Style.rsltCheckboxStyle),
          <.span(^.className := "checkbox-lbl"),
          if (!connection.name.isEmpty) {
            <.div(DashBoardCSS.Style.profileNameHolder)(connection.name)
          } else {
            <.span()
          },
          <.div(^.marginLeft := "30px")(
            <.div(^.className := "media-left", ^.float.left)(
              if (connection.imgSrc.isEmpty) {
                <.img(DashBoardCSS.Style.profileImg, ^.src := "./assets/images/default_avatar.jpg", ^.borderRadius := "25px", ^.title := "Connection Source: " + connection.connection.source + " Target: " + connection.connection.target + " Label: " + connection.connection.label)
              } else {
                <.img(DashBoardCSS.Style.profileImg, ^.src := connection.imgSrc, ^.borderRadius := "25px", ^.title := "Connection Source: " + connection.connection.source + " Target: " + connection.connection.target + " Label: " + connection.connection.label)
              }
            )
            /*
          <.div()(
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)("Software Developer"),
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)("Pune, India"),
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)("Connected since 2014-01-02"),
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)(
              "Profiles: ",
              <.a()("title".reactAttr := "Videographer")("Videographer"),
              " | ",
              <.a()("title".reactAttr := "Web Developer")("Web Developer"),
              " | ",
              <.a()("title".reactAttr := "Janal, LLC")("Janal, LLC")
            ),
            <.div(DashBoardCSS.Style.rsltProfileDetailsHolder)(
              "In My Groups: ",
              <.a()("title".reactAttr := "Film Industry")("Film Industry"),
              ", ",
              <.a()("title".reactAttr := "Full Stack Developers")("Full Stack Developers")
            )
          )*/
          ),
          <.br(),
          <.div(^.className := "media-body")(
            <.div(^.className := "col-md-12 col-sm-12 ")(
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Hide", Icon.remove),
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Favorite", Icon.star),
              <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Introduce", Icon.userTimes),
              NewRecommendation(NewRecommendation.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.thumbsOUp, "Recommend")),
              NewMessage(NewMessage.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.envelope, "Message"))
            )
          )
        )
      }
      <.div(^.className := "col-md-12 col-sm-12 col-xs-12", ^.paddingLeft := "0px", ^.paddingRight := "0px", DashBoardCSS.Style.rsltSectionContainer)(
        <.ul(^.className := "media-list")(p.connections map renderConnections)
      )
    })
    .build

  def apply(connections: Seq[ConnectionsModel]) =
    ConnectionList(ConnectionListProps(connections))
}
