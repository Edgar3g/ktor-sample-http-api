package com.example.routes

import com.example.models.orderStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*



fun Route.listOrdersRoute()
{
    get("/orders")
    {
        if (orderStorage.isNotEmpty())
        {
            call.respond(orderStorage)
        }
    }

}

fun Route.getOrderRoute() {
    get("/order/{id?}")
    {
        val id = call.parameters["id"] ?: return@get call.respondText("BadRequest", status = HttpStatusCode.BadRequest)
        val order = orderStorage.find{ it.number == id } ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)

        call.respond(order)
    }
}

fun Route.totalizeOrderRoute()
{
    get("/order/{id?}/total"){
        val id = call.parameters["id"] ?: return@get call.respondText("BadRequest", status = HttpStatusCode.BadRequest)
        val order = orderStorage.find { it.number == id } ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)

        val total = order.content.sumOf { it.price * it.amount }
        call.respond(total)
    }
}