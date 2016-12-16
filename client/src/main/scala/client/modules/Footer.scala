package client.modules

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import client.LGMain.{DashboardLoc, Loc}
import client.components.Bootstrap.PopoverOptions
import client.components._
import client.css.{DashBoardCSS, FooterCSS}
import client.modals._
import org.querki.jquery.$
import client.components.Bootstrap._
import scala.scalajs.js
import scalacss.ScalaCssReact._

object Footer {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(ctl: RouterCtl[Loc], currentLoc: Loc)

  case class FooterItem(idx: Int, label: (Props) => ReactNode, location: Loc)

  case class Backend(t: BackendScope[Props, _]) {
    def mounted(props: Props): Callback = Callback {
      val addTooltip: js.Object = ".FooterCSS_Style-displayInlineGlyph"
      $(addTooltip).tooltip(PopoverOptions.html(true))
    }
  }

  private val footerItems = Seq(
    FooterItem(1, _ => "About", DashboardLoc),
    FooterItem(2, _ => "Legal", DashboardLoc),
    FooterItem(3, _ => "LivelyGig", DashboardLoc)
  )
  private val Footer = ReactComponentB[Props]("Footer")
    .stateless
    .backend(new Backend(_))
    .render_P((P) => {
      <.nav(^.className := "footerContainer", FooterCSS.Style.footerContainer)(
        <.div(^.className := "col-lg-1")(),
        <.div(^.className := "col-lg-3 col-md-5 col-sm-5 col-xs-5", FooterCSS.Style.footPaddingLeft)(
          <.div(FooterCSS.Style.footGlyphContainer)(
            <.div(FooterCSS.Style.displayInline)(
              <.a(FooterCSS.Style.displayInlineGlyph)(^.href := "https://github.com/LivelyGig", ^.target := "_blank", "data-toggle".reactAttr := "tooltip", "title".reactAttr := "GitHub", "data-placement".reactAttr := "right")(<.span()(Icon.github))
            ),
            <.div(FooterCSS.Style.displayInline)(
              <.a(FooterCSS.Style.displayInlineGlyph)(^.href := "https://twitter.com/LivelyGig", ^.target := "_blank", "data-toggle".reactAttr := "tooltip", "title".reactAttr := "Twitter", "data-placement".reactAttr := "right")(
                <.span()(Icon.twitter)
              )
            ),
            <.div(FooterCSS.Style.displayInline)(
              <.a(FooterCSS.Style.displayInlineGlyph)(^.href := "https://www.facebook.com/LivelyGig-835593343168571/", ^.target := "_blank", "data-toggle".reactAttr := "tooltip", "title".reactAttr := "Facebook", "data-placement".reactAttr := "right")(
                <.span()(Icon.facebook)
              )
            ),
            <.div(FooterCSS.Style.displayInline)(
              <.a(FooterCSS.Style.displayInlineGlyph)(^.href := "https://plus.google.com/+LivelygigCommunity", ^.target := "_blank", "data-toggle".reactAttr := "tooltip", "title".reactAttr := "Google Plus", "data-placement".reactAttr := "right")(
                <.span()(Icon.googlePlus)
              )
            ),
            <.div(FooterCSS.Style.displayInline)(
              <.a(FooterCSS.Style.displayInlineGlyph)(^.href := "https://www.youtube.com/channel/UCBM73EEC5disDCDnvUXMe4w", ^.target := "_blank", "data-toggle".reactAttr := "tooltip", "title".reactAttr := "YouTube Channel", "data-placement".reactAttr := "right")(
                <.span()(Icon.youtube)
              )
            ),
            <.div(FooterCSS.Style.displayInline)(
              <.a(FooterCSS.Style.displayInlineGlyph)(^.href := "https://www.linkedin.com/company/10280853", ^.target := "_blank", "data-toggle".reactAttr := "tooltip", "title".reactAttr := "LinkedIn", "data-placement".reactAttr := "right")(
                <.span()(Icon.linkedin)
              )
            ),
            <.div(FooterCSS.Style.displayInline)(
              <.a(FooterCSS.Style.displayInlineGlyph, ^.id := "slack")(^.href := "https://livelygig.slack.com", ^.target := "_blank", "data-toggle".reactAttr := "tooltip", ^.title := "Slack", "data-placement".reactAttr := "right")(
                <.span()(Icon.slack)
              )
            )
          )
        ),
        <.div(^.className := "col-lg-7 col-md-7 col-sm-7 col-xs-7", DashBoardCSS.Style.paddingRight0px)(
          <.ul(^.className := "nav", FooterCSS.Style.footRight)(
            // build a list of menu items
            for (item <- footerItems) yield {
              <.li(^.className := "pull-left")(^.key := item.idx, (P.currentLoc == item.location) ?= FooterCSS.Style.footerNavLi,
                if (item.idx == 3) {
                  P.ctl.link(item.location)(FooterCSS.Style.footerNavA, " ", Icon.copyright, item.label(P))
                } else if (item.idx == 2) {
                  Legal(Legal.Props("Legal", Seq(), "", ""))
                } else {
                  P.ctl.link(item.location)(FooterCSS.Style.footerNavA, " ", item.label(P))
                })
            }
          )
        ),
        <.div(^.className := "col-lg-1")
      )
    })
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(props: Props) = Footer(props)
}