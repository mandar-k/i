package utils

import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import play.filters.headers.SecurityHeadersFilter

/**
 * Provides filters.
 */
class Filters(securityHeadersFilter: SecurityHeadersFilter) extends HttpFilters {
  override def filters: Seq[EssentialFilter] = Seq(securityHeadersFilter)
}
