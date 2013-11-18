<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>GitHub Repositories Viewer: Search</title>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'repositories.css')}" type="text/css">
		<g:javascript src="search.js" />
	</head>
	<body>
		<div id="page-body" role="main">
			<h1>Search</h1>
			<p>Enter the search phrase into the field below:</p>
			<g:formRemote name="searchForm" url="[action: 'search']" update="results"
					onSuccess="updateAddressBar('${createLink(absolute: true)}')"
					onFailure="showError()"
					onLoading="showSpinner()"
					onComplete="hideSpinner()"
					class="navbar-form pull-left">
				<g:textField name="q" value="${params.q}" />
				<g:actionSubmit value="Search" class="btn" />
			</g:formRemote>
			<div id="results">
				<g:include action="search" />
			</div>
		</div>
	</body>
</html>
