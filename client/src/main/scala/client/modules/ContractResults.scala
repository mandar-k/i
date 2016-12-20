package client.modules

import client.components.Bootstrap._
import japgolly.scalajs.react.vdom.prefix_<^._
import client.components.Icon
import client.css.{DashBoardCSS, HeaderCSS, PresetsCSS}
import client.modals.{NewMessage, WorkContractModal}
import japgolly.scalajs.react

import scala.scalajs.js
import scalacss.ScalaCssReact._
import japgolly.scalajs.react._
import org.querki.jquery.$

object ContractResults {

  case class Props()

  case class State(menuAction : String = "" ,sortMenuBy  : String = "")

  class Backend(t: BackendScope[ContractResults.Props, State]) {
    def mounted(props: Props):Callback = Callback {
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
  }

  // create the React component for Dashboard
  val component = ReactComponentB[Props]("ContractResults")
    .initialState(State())
    .backend(new Backend(_))
    .renderPS((t, props, S) => {
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
                  <.li()(<.a(^.onClick ==> t.backend.dropDownSelectAction)("Hide")),
                  <.li()(<.a(^.onClick ==> t.backend.dropDownSelectAction)("Favorite")),
                  <.li()(<.a(^.onClick ==> t.backend.dropDownSelectAction)("Unhide")),
                  <.li()(<.a(^.onClick ==> t.backend.dropDownSelectAction)("Unfavorite"))
                )
              ), <.div(PresetsCSS.Style.modalBtn)(
                WorkContractModal(WorkContractModal.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.edit, "Create Contract")),
                <.div(PresetsCSS.Style.overlay, ^.top:="0.1em")(
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
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownMenuSorting)("By Date")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownMenuSorting)("By Experience")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownMenuSorting)("By Reputation")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownMenuSorting)("By Rate")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownMenuSorting)("By Projects Completed"))
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
          <.div(^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.padding0px,DashBoardCSS.Style.rsltSectionContainer)(
            <.ul(^.className := "media-list")(
              for (i <- 1 to 50) yield {
                <.li(^.className := "media",DashBoardCSS.Style.profileDescription, DashBoardCSS.Style.rsltpaddingTop10p)(
                  <.div(^.className := "media-body")(
                    <.input(^.`type` := "checkbox", DashBoardCSS.Style.rsltCheckboxStyle),
                    <.div(DashBoardCSS.Style.profileNameHolder)("Buyer : Pam Seller: Abed Project/Offering: Project One"),
                    <.div()("lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s"),
                    <.br(),
                    "Status: Initiating   11:20am 12/08/2015",
                    <.div(^.className := "col-md-12 col-sm-12 ")(
                      <.div( /*^.className:="profile-action-buttons"*/ )(
                        <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Hide", Icon.remove),
                        <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Favorite", Icon.star),
                        WorkContractModal(WorkContractModal.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.fileTextO, "Manage")),
                        NewMessage(NewMessage.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.envelope, "Message"))
                      )
                    )
                  )
                )
              }
            )
          )
        )
      )})
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build
}

