package com.gaboardi.githubtest.model.stargazers

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class StargazerResult(
    var users: List<Stargazer> = listOf(),
    var message: String? = null
){
    object AdapterFactory: TypeAdapterFactory {
        override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
            val delegate = gson?.getDelegateAdapter(this, type)
            val elementAdapter = gson?.getAdapter(JsonElement::class.java)
            return type?.let {
                if (!StargazerResult::class.java.isAssignableFrom(it.rawType)) null else StargazerAdapter<T>(delegate, elementAdapter) as TypeAdapter<T>
            }
        }
    }

    private class StargazerAdapter<T : Any?>(val delegate: TypeAdapter<T>?, val elementAdapter: TypeAdapter<JsonElement>?): TypeAdapter<StargazerResult>(){
        override fun write(out: JsonWriter?, value: StargazerResult?) {
            if(out != null && value != null)
                delegate?.write(out, value as T)
        }

        override fun read(`in`: JsonReader?): StargazerResult? {
            val stargazers = StargazerResult()
            val element = elementAdapter?.read(`in`)
            return element?.let {
                if(it.isJsonArray){
                    val type = object: TypeToken<List<Stargazer>>(){}.type
                    stargazers.users = Gson().fromJson<List<Stargazer>>(it.toString(), type)
                    stargazers.message = null
                    stargazers
                }else{
                    stargazers.users = listOf()
                    stargazers.message = it.asJsonObject["message"].asString
                    stargazers
                }
            }
        }
    }
}