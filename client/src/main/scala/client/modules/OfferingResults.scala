package client.modules

import client.components.Bootstrap._
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.ReactComponentB
import client.components.Icon
import client.css.{DashBoardCSS, HeaderCSS, PresetsCSS}
import client.modals.{NewMessage, NewRecommendation, Offering}
import japgolly.scalajs.react
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.querki.jquery.$

import scala.scalajs.js
import scalacss.ScalaCssReact._


object OfferingResults {
  case class Props()

  case class State()

  class Backend(t: BackendScope[Props, State]) {
    def mounted(props: Props) = Callback {
      //      log.debug("connection view mounted")
      //      Callback.when(props.proxy().isEmpty)(props.proxy.dispatch(RefreshConnections()))
      val addTooltip: js.Object = ".DashBoardCSS_Style-btn"
      $(addTooltip).tooltip(PopoverOptions.html(true))
    }
    def dropDownSelected(event: ReactEventI): react.Callback = Callback {
      val value = event.target.innerHTML
      event.target.parentElement.parentElement.previousElementSibling.firstChild.textContent=value
    }
  }


  val component = ReactComponentB[Props]("Offerings")
    .initialState(State())
    .backend(new Backend(_))
    .renderPS((t, props, S) => {
      <.div(^.id := "rsltScrollContainer", DashBoardCSS.Style.rsltContainer)(
        <.div(DashBoardCSS.Style.gigActionsContainer, ^.className := "row")(
     /*     <.div(^.className := "col-md-6 col-sm-6 col-xs-12")(
            <.input(^.`type` := "checkbox", DashBoardCSS.Style.rsltCheckboxStyle),
            <.div(^.display := "inline-block")(
              <.div(DashBoardCSS.Style.displayInlineText, ^.className := "dropdown")(
                <.button(DashBoardCSS.Style.gigMatchButton, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown")(
                  <.span("Select Bulk Action "))(
                  <.span(^.className := "caret", DashBoardCSS.Style.rsltCaretStyle)
                ),
                <.ul(^.className := "dropdown-menu")(
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("Hide")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("Favorite")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("Unhide")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("Unfavorite"))
                )
              ),
              <.div(PresetsCSS.Style.modalBtn)(
                Offering(Offering.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.briefcase, "Create Offering")),
                // NewMessage(NewMessage.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.envelope, "Create New Message")),
                <.div(PresetsCSS.Style.overlay)(
                  Icon.plus
                )
              ),
              <.div(DashBoardCSS.Style.displayInlineText, DashBoardCSS.Style.rsltCountHolderDiv, DashBoardCSS.Style.marginResults)("2,352 Results")
            )
          ),*/   <.div(^.className := "col-md-4 col-sm-4 col-xs-8")(
            <.input(^.`type` := "checkbox", DashBoardCSS.Style.rsltCheckboxStyle),
            <.div(^.display := "inline-block")(
              <.div(DashBoardCSS.Style.displayInlineText, ^.className := "dropdown")(
                <.button(DashBoardCSS.Style.gigMatchButton, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown")(
                  <.span("Select Bulk Action "))(
                  <.span(^.className := "caret", DashBoardCSS.Style.rsltCaretStyle)
                ),
                <.ul(^.className := "dropdown-menu")(
                  <.li()(<.a(^.onClick ==> t.backend.dropDownSelected)("Hide")),
                  <.li()(<.a(^.onClick ==> t.backend.dropDownSelected)("Favorite")),
                  <.li()(<.a(^.onClick ==> t.backend.dropDownSelected)("Unhide")),
                  <.li()(<.a(^.onClick ==> t.backend.dropDownSelected)("Unfavorite"))
                )
              ), <.div(PresetsCSS.Style.modalBtn)(
                Offering(Offering.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.briefcase, "Create Offering")),
                // NewMessage(NewMessage.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.envelope, "Create New Message")),
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
                <.button(DashBoardCSS.Style.gigMatchButton, ^.className := "btn dropdown-toggle", "data-toggle".reactAttr := "dropdown")(
                  <.span("By Date "))(
                  <.span(^.className := "caret", DashBoardCSS.Style.rsltCaretStyle)
                ),
                <.ul(^.className := "dropdown-menu")(
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("By Date")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("By Experience")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("By Reputation")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("By Rate")),
                  <.li()(<.a(^.onClick ==>  t.backend.dropDownSelected)("By Projects Completed"))
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
          //          <.div(^.className := "rsltSectionContainer", ^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.padding0px)(
          <.div(DashBoardCSS.Style.rsltSectionContainer, ^.className := "col-md-12 col-sm-12 col-xs-12", DashBoardCSS.Style.padding0px)(
            <.ul(^.className := "media-list")(
              for (i <- 1 to 50) yield {
                // <.li(^.className := "media profile-description", DashBoardCSS.Style.rsltpaddingTop10p)(
                <.li(^.className := "media",DashBoardCSS.Style.profileDescription, DashBoardCSS.Style.rsltpaddingTop10p)(
                  <.input(^.`type` := "checkbox", DashBoardCSS.Style.rsltCheckboxStyle),
                  <.span(^.className := "checkbox-lbl"),
                  <.div(DashBoardCSS.Style.profileNameHolder)("New company branding - icon and website... $250.  Posted  11:00am 12/05/2015"),
                  <.div(^.className := "media-body")(
                    "lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
                    <.br(),
                    "Recommended By: Tom",
                    <.div()(
                      <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Hide", Icon.remove),
                      <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Favorite", Icon.star),
                      NewRecommendation(NewRecommendation.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.thumbsOUp, "Recommend")),
                      <.button(^.tpe := "button", ^.className := "btn profile-action-buttons pull-right", HeaderCSS.Style.rsltContainerIconBtn, ^.title := "Purchase", Icon.cartPlus),
                      NewMessage(NewMessage.Props("", Seq(HeaderCSS.Style.rsltContainerIconBtn), Icon.envelope, "Messages"))
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

