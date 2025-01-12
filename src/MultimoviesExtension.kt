import com.lagradost.cloudstream3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class MultimoviesExtension : Source {
    override val name = "Multimovies"
    override val lang = "en"
    override val mainUrl = "https://multimovies.today"

    override val hasMainPage = true
    override val hasSearch = true

    override fun setup() {}

    override fun fetchMovies(): List<Movie> {
        val url = "$mainUrl/movies"
        val doc: Document = Jsoup.connect(url).get()
        val moviesList = mutableListOf<Movie>()

        val movieElements = doc.select(".movies-container .movie") // Update with the correct CSS selector
        movieElements.forEach { element ->
            val title = element.select(".movie-title").text()
            val imageUrl = element.select("img").attr("src")
            val movieUrl = element.select("a").attr("href")

            moviesList.add(
                Movie(
                    title = title,
                    url = movieUrl,
                    imageUrl = imageUrl
                )
            )
        }
        return moviesList
    }

    override fun fetchSeries(): List<Series> {
        val url = "$mainUrl/series"
        val doc: Document = Jsoup.connect(url).get()
        val seriesList = mutableListOf<Series>()

        val seriesElements = doc.select(".series-container .series") // Update with the correct CSS selector
        seriesElements.forEach { element ->
            val title = element.select(".series-title").text()
            val imageUrl = element.select("img").attr("src")
            val seriesUrl = element.select("a").attr("href")

            seriesList.add(
                Series(
                    title = title,
                    url = seriesUrl,
                    imageUrl = imageUrl
                )
            )
        }
        return seriesList
    }

    override fun search(query: String): List<Any> {
        val url = "$mainUrl/search?q=$query"
        val doc: Document = Jsoup.connect(url).get()
        val searchResults = mutableListOf<Any>()

        val movieElements = doc.select(".search-movie-item") // Modify with actual HTML structure
        movieElements.forEach { element ->
            val title = element.select(".movie-title").text()
            val movieUrl = element.select("a").attr("href")
            searchResults.add(Movie(title = title, url = movieUrl))
        }

        val seriesElements = doc.select(".search-series-item") // Modify with actual HTML structure
        seriesElements.forEach { element ->
            val title = element.select(".series-title").text()
            val seriesUrl = element.select("a").attr("href")
            searchResults.add(Series(title = title, url = seriesUrl))
        }

        return searchResults
    }
}

class Movie(
    val title: String,
    val url: String,
    val imageUrl: String
) : Searchable {
    override val searchUrl = url
}

class Series(
    val title: String,
    val url: String,
    val imageUrl: String
) : Searchable {
    override val searchUrl = url
}
