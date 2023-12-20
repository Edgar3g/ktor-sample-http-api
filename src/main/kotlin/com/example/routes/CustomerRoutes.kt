package com.example.routes

import com.example.models.Customer
import com.example.models.custumerStorage
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.request.*

fun Route.customerRouting(){
    route("/customer"){
        get {
            if (custumerStorage.isNotEmpty())
            {
                call.respond(custumerStorage)
            } else { call.respondText("No customers found", status = HttpStatusCode.OK) }
        }

        get("{id?}"){
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                custumerStorage.find { it.id == id } ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }

        post {
            val customer = call.receive<Customer>()
            custumerStorage.add(customer)
            call.respondText("Customer Stored correctly", status = HttpStatusCode.Created)
        }

        delete("{id?}"){
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (custumerStorage.removeIf { it.id == id }) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            }
            else { call.respondText("Not Found", status = HttpStatusCode.NotFound) }
        }
    }
}