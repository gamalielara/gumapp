package com.gumrindelwald.network.run_network_utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.gumrindelwald.core.domain.delete
import com.gumrindelwald.core.domain.get
import com.gumrindelwald.core.domain.safeCall
import com.gumrindelwald.core.domain.util.DataError
import com.gumrindelwald.core.domain.util.EmptyResult
import com.gumrindelwald.core.domain.util.Result
import com.gumrindelwald.core.domain.util.map
import com.gumrindelwald.domain.run.RemoteRunDataSource
import com.gumrindelwald.domain.run.Run
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json


class KtorRemoteRunDataSource(
    private val httpClient: HttpClient
) : RemoteRunDataSource {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getRuns(): Result<List<Run>, DataError.Network> {
        return httpClient.get<List<RunDTO>>(route = "/runs")
            .map { runDTOS -> runDTOS.map { it.toRun() } }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun postRun(
        run: Run, mapPicture: ByteArray
    ): Result<Run, DataError.Network> {
        val createRunRequestJSON = Json.encodeToString(run.toRunRequest())

        val result = safeCall<RunDTO> {
            httpClient.submitFormWithBinaryData(
                url = "/run",
                formData = formData {
                    append("MAP_PICTURE", mapPicture, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(
                            HttpHeaders.ContentDisposition,
                            "attachment; filename=map_picture.jpg"
                        )
                    })

                    append("RUN_DATA", createRunRequestJSON, Headers.build {
                        append(HttpHeaders.ContentType, "text/plain")
                        append(
                            HttpHeaders.ContentDisposition,
                            "form-data; name=\"RUN_DATA\""
                        )
                    })
                }
            ) {
                method = HttpMethod.Post
            }

        }

        return result.map { it.toRun() }
    }

    override suspend fun deleteRun(runId: String): EmptyResult<DataError.Network> {
        return httpClient.delete<Unit>("/run", queryParameters = mapOf("id" to runId))
    }
}