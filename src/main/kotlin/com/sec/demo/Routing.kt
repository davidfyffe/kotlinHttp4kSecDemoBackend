package com.sec.demo

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.lens.Query
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

val usernameLens = Query.required(("username"))
val passwordLens = Query.required("password")

fun main(args: Array<String>) {

    val app = ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive).then(
            routes(
                    "/login" bind Method.GET to authUser()
            )
    )

    app.asServer(Jetty(8008)).start()


    println("running local")

}


private fun authUser(): HttpHandler = { req ->

    val username = usernameLens(req)
    val password = passwordLens(req)

    if (username.toLowerCase() == "admin" && password == "orange12") {
        Response.invoke(OK).body("Your logged in as $username")
    } else {
        Response.invoke(UNAUTHORIZED)
    }

}

