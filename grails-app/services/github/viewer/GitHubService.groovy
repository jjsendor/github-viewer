package github.viewer

import grails.plugins.rest.client.RestBuilder

class GitHubService {

	static transactional = false

    def apiRoot = "https://api.github.com"
    def rest = new RestBuilder()
    def maxCommits = 100

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
