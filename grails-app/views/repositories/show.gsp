<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	    <script type="text/javascript">
	      google.load("visualization", "1", {packages:["corechart"]});
      	<g:if test="${!request.commitsPerUser.isEmpty()}">
	      google.setOnLoadCallback(drawImpactChart);

	      function drawImpactChart() {
	        var data = google.visualization.arrayToDataTable([
	          ['User', 'Commits'],${raw(request.commitsPerUser.collect({ "['$it.key', $it.value]" }).join(', '))}
	        ]);

	        var options = {
	          title: 'Commits per user'
	        };

	        var chart = new google.visualization.PieChart(document.getElementById('impact_chart'));
	        chart.draw(data, options);
	      }
		</g:if>

		<g:if test="${!request.commitsPerDate.isEmpty()}">
	      google.setOnLoadCallback(drawTimelineChart);
	      function drawTimelineChart() {
	        var data = new google.visualization.DataTable();
	        data.addColumn('date', 'Date');
			data.addColumn('number', 'Commits');
			data.addRows([
				${raw(request.commitsPerDate.collect({ "[$it.key, $it.value]" }).join(', '))}
	        ]);

	        var options = {
	          title: 'Project timeline'
	        };

	        var chart = new google.visualization.ColumnChart(document.getElementById('timeline_chart'));
	        chart.draw(data, options);
	      }
		</g:if>
	    </script>
		<title>GitHub Repositories Viewer: ${params.user}/${params.repo}</title>
	</head>
	<body>
		<div class="page-header">
			<h1>Repository details <small>${params.user}/${params.repo}</small></h1>
		</div>
		<div id="page-body" role="main">
			<h2>Contributors</h2>
			<g:if test="${request.contributors.empty}">
				<div class="alert">
					<p class="no_contributors">No contributors</p>
				</div>
			</g:if>
			<g:else>
				<p class="contributors">${request.contributors.join(", ")}</p>
			</g:else>

			<h2>Project impact by user (based on 100 latest commits)</h2>
			<g:if test="${request.commitsPerUser.isEmpty()}">
				<div class="alert">
					<p class="no_commits">No commits</p>
				</div>
			</g:if>
			<g:else>
				<div id="impact_chart" style="width: 900px; height: 500px;"></div>
			</g:else>
			<h2>Project timeline (based on 100 latest commits)</h2>
			<g:if test="${request.commitsPerDate.isEmpty()}">
				<div class="alert">
					<p class="no_commits">No commits</p>
				</div>
			</g:if>
			<g:else>
				<div id="timeline_chart" style="width: 900px; height: 500px;"></div>
			</g:else>
		</div>
	</body>
</html>
