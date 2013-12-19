package github.viewer

/**
 * Actions related to the GitHub repositories, search and displaying
 * simple stats (contributors, analytics based on last 100 commits).
 */
class RepositoriesController {

	def gitHubService

    // default size of the search response list
	def responsesPerPage = 20

    /**
     * Main controller action. Stays empty as all of the search related processing
     * is delegated to search action.
     */
    def index() { }

    /**
     * Display search results or empty page depending on query <code>q</code> parameter.
     * Optionally accepts <code>offset</code> and <code>max</code> parameters
     * that make it possible to skip a number of first results or limit the result list
     * respectively.
     */
    def search() {
    	def q = params.q
        // default offset is 0
    	def offset = params.offset ? (params.offset as int) : 0
    	def max = params.max ? (params.max as int) : responsesPerPage

    	if (q == null) {
    		render ""
    	}
    	else {
            def results = gitHubService.searchRepositories(q, offset, max)

            if (results == null) {
                render 'Error while fetching the search results. Please try again later.'
            }
            else {
                request.results = results
                request.max = max
            }
        }
    }

    /**
     * Display page with repository analytics.
     * Repository is indeitfied by <code>user</code> and <code>repo</code>
     * parameters.
     */
    def show() {
        def owner = params.user
        def repo = params.repo

        def contributors = gitHubService.listContributors(owner, repo)

        if (contributors == null) {
            request.contributors = []
        }
        else {
            // map the JSON array with contributors list to
            // the list of logins
            request.contributors = contributors.collect({ it.login })
        }

        def commits = gitHubService.listCommits(owner, repo)

        if (commits == null) {
            request.commitsPerUser = [:]
            request.commitsPerDate = [:]
        }
        else {
            // group by the commit author name
            // (will return a map of [commiter name : # of commits])
            request.commitsPerUser = commits.countBy { it.commit.author.name }
            // groub by the commit date
            request.commitsPerDate = commits.countBy {
                // first ignore the time in the timestamp
                // and take only date into account
                it.commit.author.date.substring(0, 10)
            } collectEntries { key, value ->
                // modify the date string to represent Google Chart date format
                def date = new Date().parse("yyyy-MM-dd", key)
                def year = date.format("yyyy")
                def month = (date.format("M") as int) - 1
                def day = date.format("d")
                ["new Date($year, $month, $day)" as String, value]
            }
        }
    }
}
