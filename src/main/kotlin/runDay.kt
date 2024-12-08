import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.createFile
import kotlin.io.path.readText
import kotlin.io.path.writeText

fun <I, O> runDay(
    day: Int,
    partOne: ((I) -> O)? = null,
    partTwo: ((I) -> O)? = null,

    inputOverride: (() -> String)? = null,

    parseInput: (String) -> I,
) {

    val inputStr = inputOverride?.invoke() ?: getInputString(day)
    val input = parseInput(inputStr)

    println("-- Day $day --")

    if (partOne == null && partTwo == null) {
        println("No solution provided, printing parsed input")
        println(input)
    }
    else {
        val result1 = partOne?.invoke(input)
        println("Part one: ${result1 ?: "TODO"}")

        val result2 = partTwo?.invoke(input)
        println("Part two: ${result2 ?: "TODO"}")
    }
}

private fun getInputString(day: Int): String {
    check(day in 1..25) { "Invalid day" }

    val path = getDayInputPath(day)

    return if (Files.exists(path)) path.readText()
    else downloadInput(day)
}

private fun getDayInputPath(day: Int) = Paths.get(resDir, day.toString())

private val resDir = run {
    val projectDir = System.getProperty("user.dir")
    val resDir = "\\src\\main\\resources\\"
    "$projectDir$resDir"
}

private val sessionToken: String get() {
    val sessionPath = Paths.get(resDir, "session")
    check(Files.exists(sessionPath)) { "No session token, cannot download input" }
    return sessionPath.readText()
}

private fun downloadInput(day: Int): String {
    println("Downloading input for day $day")

    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI("https://adventofcode.com/2024/day/${day}/input"))
        .header("Cookie", "session=$sessionToken")
        .build()

    return client.send(request, BodyHandlers.ofString())
        .body()
        .trim('\n')
        .also { input ->
            val path = getDayInputPath(day).createFile()
            path.writeText(input)
        }
}