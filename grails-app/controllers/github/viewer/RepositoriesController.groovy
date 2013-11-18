package github.viewer

class RepositoriesController {

	def gitHubService

	def responsesPerPage = 20

    def index() { }

    def search() {
    	def q = params.q
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

    def show() {
        def owner = params.user
        def repo = params.repo

        def contributors = gitHubService.listContributors(owner, repo)

        if (contributors == null) {
            request.contributors = []
        }
        else {
            request.contributors = contributors.collect({ it.login })
        }

        def commits = gitHubService.listCommits(owner, repo)

        if (commits == null) {
            request.commitsPerUser = [:]
            request.commitsPerDate = [:]
        }
        else {
            request.commitsPerUser = commits.countBy { it.commit.author.name }
            request.commitsPerDate = commits.countBy {
                it.commit.author.date.substring(0, 10)
            } collectEntries { key, value ->
                def date = new Date().parse("yyyy-MM-dd", key)
                def year = date.format("yyyy")
                def month = (date.format("M") as int) - 1
                def day = date.format("d")
                ["new Date($year, $month, $day)" as String, value]
            }
        }
    }
}
