package com.gaboardi.githubtest.model.userrepos

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.google.gson.JsonElement

class RepoResult(
    var repos: List<Repo> = listOf(),
    var message: String? = null
){
    object AdapterFactory: TypeAdapterFactory{
        override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
            val delegate = gson?.getDelegateAdapter(this, type)
            val elementAdapter = gson?.getAdapter(JsonElement::class.java)
            return type?.let {
                if (!RepoResult::class.java.isAssignableFrom(it.rawType)) null else RepoAdapter<T>(delegate, elementAdapter) as TypeAdapter<T>
            }
        }
    }

    private class RepoAdapter<T : Any?>(val delegate: TypeAdapter<T>?, val elementAdapter: TypeAdapter<JsonElement>?): TypeAdapter<RepoResult>(){
        override fun write(out: JsonWriter?, value: RepoResult?) {
            if(out != null && value != null)
                delegate?.write(out, value as T)
        }

        override fun read(`in`: JsonReader?): RepoResult? {
            val repo = RepoResult()
            val element = elementAdapter?.read(`in`)
            return element?.let {
                if(it.isJsonArray){
                    val type = object: TypeToken<List<Repo>>(){}.type
                    repo.repos = Gson().fromJson<List<Repo>>(it.toString(), type)
                    repo.message = null
                    repo
                }else{
                    repo.repos = listOf()
                    repo.message = it.asJsonObject["message"].asString
                    repo
                }
            }
        }
    }
}