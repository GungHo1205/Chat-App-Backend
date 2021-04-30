import play.mvc.EssentialFilter;
import play.filters.cors.CORSFilter;
import play.http.HttpFilters;
import javax.inject.Inject;
/* FROM
https://stackoverflow.com/questions/38370308/how-to-impliment-cross-origin-resource-sharing-cors-in-play-framework-2-5-x
 */
public class Filters implements HttpFilters {

    @Inject
    CORSFilter corsFilter;

    public EssentialFilter[] filters() {
        return new EssentialFilter[] { corsFilter.asJava() };
    }
}