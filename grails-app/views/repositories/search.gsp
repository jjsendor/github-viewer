<h2>Search results for: <span class="search_query">${params.q}</span></h2>
<p>Total results: <span class="search_total">${request.results.total_count}</span></p>
<ol start="${params.offset ? (params.offset as int) + 1 : 1}" class="results_list">
<g:each in="${request.results.items}" var="repo">
	<li>
		<g:link params="[user: repo.owner.login, repo: repo.name]"
			 action="show">${repo.name}</g:link>
		<a href="${repo.html_url}" class="repo_link">${repo.html_url}</a>
	</li>
</g:each>
</ol>
<div class="pagination">
	<g:paginate next="Next" prev="Previous" action="index" params="${[q: params.q]}" max="${request.max}"
            total="${Math.min(request.results.total_count, 1000)}" />
</div>
