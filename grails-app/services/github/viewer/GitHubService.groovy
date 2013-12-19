package github.viewer

import grails.plugins.rest.client.RestBuilder

/**
 * Communication with GitHub API: http://developer.github.com/v3/
 */
class GitHubService {

	static transactional = false

    // GitHub API root address used to build queries
    def apiRoot = "https://api.github.com"
    def rest = new RestBuilder()
    // maximum number of commits fetched in queries
    def maxCommits = 100

    /**
     * Searches repositories based on query <code>q</code>.
     * Return JSON entity with search results.
     */
    def searchRepositories(q, offset, max) {
		def page = offset / max + 1
    	def response

    	try {
            log.info "Fetching search results for query: ${q}, page: ${page}"
    		response = rest.get("${apiRoot}/search/repositories?q=${q}&page=${page}&per_page=${max}")
    	}
		catch (Exception e) {
			log.error("Exception while retrieving GitHub Search API response: ${e.message}")
			return null
		}

    	if (response.status == 200) {
    		response.json
    	}
    	else {
            log.error "Error while fetching search results (${response.status}): ${response.text}"
    		return null
    	}
    }

    /**
     * Lists contributors for a given repository based on
     * <code>owner</code> and <code>repo</code> parameters.
     */
    def listContributors(owner, repo) {
        def response

        try {
            log.info "Fetching contributors for repository ${owner}/${repo}"
            response = rest.get("${apiRoot}/repos/${owner}/${repo}/contributors")
        }
        catch (Exception e) {
            log.error("Exception while retrieving GitHub Repos API response: ${e.message}")
            return null
        }

        if (response.status == 200) {
            response.json
        }
        else {
            log.error "Error while fetching contributors list (${response.status}): ${response.text}"
            return null
        }
    }

    /**
     * Lists last {@see #maxCommits} commits for a given repository identified by
     * <code>owner</code> and <code>repo</code> parameters.
     */
    def listCommits(owner, repo) {
        def response

        try {
            log.info "Fetching commits for repository ${owner}/${repo}"
            response = rest.get("${apiRoot}/repos/${owner}/${repo}/commits?per_page=${maxCommits}")
        }
        catch (Exception e) {
            log.error("Exception while retrieving GitHub Repos API response: ${e.message}")
            return null
        }

        if (response.status == 200) {
            response.json
        }
        else {
            log.error "Error while fetching commits list (${response.status}): ${response.text}"
            return null
        }
    }

}
