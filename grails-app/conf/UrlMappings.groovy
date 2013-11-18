class UrlMappings {

	static mappings = {
        "/repositories/show/$user/$repo" {
        	controller = 'repositories'
        	action = 'show'
        }
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
