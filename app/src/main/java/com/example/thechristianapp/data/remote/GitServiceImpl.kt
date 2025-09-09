package com.example.thechristianapp.data.remote

import android.content.Context
import android.util.Log
import com.example.thechristianapp.data.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GitService {
    
    companion object {
        private const val TAG = "GitService"
        private const val BIBLE_ASSETS_PATH = "bible"
    }
    
    override suspend fun loadBibleFromAssets(): Result<Triple<List<Book>, List<Chapter>, List<Verse>>> = 
        withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Loading Bible data from assets...")
            
            // Get list of markdown files from assets
            val assetManager = context.assets
            val bibleFiles: Array<String> = try {
                assetManager.list(BIBLE_ASSETS_PATH) ?: emptyArray()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to access assets directory: $BIBLE_ASSETS_PATH", e)
                return@withContext Result.failure(e)
            }
            
            if (bibleFiles.isEmpty()) {
                Log.w(TAG, "No Bible files found in assets directory: $BIBLE_ASSETS_PATH")
                return@withContext Result.failure(Exception("No Bible files found in assets"))
            }
            
            Log.d(TAG, "Found ${bibleFiles.size} files in assets")
            
            // Parse markdown files to extract Bible content
            val bibleData = parseAssetMarkdownFiles(bibleFiles.sorted().toTypedArray())
            
            Log.d(TAG, "Successfully loaded ${bibleData.first.size} books from assets")
            Result.success(bibleData)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load Bible data from assets", e)
            Result.failure(e)
        }
    }
    
    private fun parseAssetMarkdownFiles(bibleFiles: Array<String>): Triple<List<Book>, List<Chapter>, List<Verse>> {
        val books = mutableListOf<Book>()
        val chapters = mutableListOf<Chapter>()
        val verses = mutableListOf<Verse>()
        
        var bookId = 1
        var chapterId = 1
        var verseId = 1
        
        bibleFiles.forEach { fileName ->
            if (fileName.endsWith(".md")) {
                try {
                    val content = readAssetFile("$BIBLE_ASSETS_PATH/$fileName")
                    val bookName = extractBookName(fileName)
                    val bookNumber = extractBookNumber(fileName)
                    val testament = if (bookNumber <= 39) "OLD" else "NEW"
                    
                    Log.d(TAG, "Processing book: $bookName (Book #$bookNumber, Testament: $testament)")
                    
                    val book = Book(
                        id = bookId,
                        name = bookName,
                        testament = testament,
                        abbreviation = getBookAbbreviation(bookName),
                        chapterCount = 0
                    )
                    books.add(book)
                    
                    // Parse chapters and verses from markdown content
                    val (chaptersInBook, versesInBook) = parseMarkdownContent(content, bookId, chapterId, verseId)
                    chapters.addAll(chaptersInBook)
                    verses.addAll(versesInBook)
                    
                    Log.d(TAG, "Book '$bookName': ${chaptersInBook.size} chapters, ${versesInBook.size} verses")
                    
                    // Update book chapter count
                    books[books.size - 1] = book.copy(chapterCount = chaptersInBook.size)
                    
                    bookId++
                    chapterId += chaptersInBook.size
                    verseId += versesInBook.size
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing file $fileName", e)
                }
            }
        }
        
        return Triple(books, chapters, verses)
    }
    
    private fun readAssetFile(filePath: String): String {
        return context.assets.open(filePath).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readText()
            }
        }
    }
    
    private fun extractBookName(fileName: String): String {
        // Extract book name from filename like "01 - Genesis - KJV.md"
        return fileName.substringAfter(" - ").substringBefore(" - KJV.md")
    }
    
    private fun extractBookNumber(fileName: String): Int {
        // Extract book number from filename like "01 - Genesis - KJV.md"
        return fileName.substringBefore(" ").toIntOrNull() ?: 1
    }
    
    private fun parseMarkdownContent(
        content: String,
        bookId: Int,
        startChapterId: Int,
        startVerseId: Int
    ): Pair<List<Chapter>, List<Verse>> {
        val chapters = mutableListOf<Chapter>()
        val verses = mutableListOf<Verse>()
        
        val lines = content.split("\n")
        
        var currentChapterId = startChapterId
        var currentVerseId = startVerseId
        var currentChapterNumber = 0
        
        lines.forEachIndexed { index, line ->
            val trimmedLine = line.trim()
            when {
                // Chapter header: ## Genesis Chapter 1 or ## Matthew Chapter 1
                trimmedLine.startsWith("## ") && trimmedLine.contains(" Chapter ") -> {
                    val chapterNumber = trimmedLine.substringAfterLast("Chapter ").toIntOrNull()
                    if (chapterNumber != null) {
                        chapters.add(Chapter(currentChapterId, bookId, chapterNumber))
                        currentChapterNumber = chapterNumber
                        currentChapterId++
                        Log.d(TAG, "Found chapter: $chapterNumber in line: $trimmedLine")
                    }
                }
                // Verse content: starts with number followed by space
                trimmedLine.matches(Regex("^\\d+ .+")) && currentChapterNumber > 0 -> {
                    val verseNumber = trimmedLine.substringBefore(" ").toIntOrNull()
                    val verseText = trimmedLine.substringAfter(" ")
                    if (verseNumber != null && verseText.isNotEmpty() && chapters.isNotEmpty()) {
                        // Use the current chapter ID (the last added chapter)
                        val currentChapterDbId = currentChapterId - 1
                        verses.add(Verse(currentVerseId, currentChapterDbId, verseNumber, verseText))
                        currentVerseId++
                    }
                }
            }
        }
        
        return Pair(chapters, verses)
    }
    
    private fun getBookAbbreviation(bookName: String): String {
        return when (bookName.lowercase()) {
            "genesis" -> "Gen"
            "exodus" -> "Exo"
            "leviticus" -> "Lev"
            "numbers" -> "Num"
            "deuteronomy" -> "Deu"
            "joshua" -> "Jos"
            "judges" -> "Jud"
            "ruth" -> "Rut"
            "1 samuel" -> "1Sa"
            "2 samuel" -> "2Sa"
            "1 kings" -> "1Ki"
            "2 kings" -> "2Ki"
            "1 chronicles" -> "1Ch"
            "2 chronicles" -> "2Ch"
            "ezra" -> "Ezr"
            "nehemiah" -> "Neh"
            "esther" -> "Est"
            "job" -> "Job"
            "psalms" -> "Psa"
            "proverbs" -> "Pro"
            "ecclesiastes" -> "Ecc"
            "the song of solomon" -> "Son"
            "song of solomon" -> "Son"
            "isaiah" -> "Isa"
            "jeremiah" -> "Jer"
            "lamentations" -> "Lam"
            "ezekiel" -> "Eze"
            "daniel" -> "Dan"
            "hosea" -> "Hos"
            "joel" -> "Joe"
            "amos" -> "Amo"
            "obadiah" -> "Oba"
            "jonah" -> "Jon"
            "micah" -> "Mic"
            "nahum" -> "Nah"
            "habakkuk" -> "Hab"
            "zephaniah" -> "Zep"
            "haggai" -> "Hag"
            "zechariah" -> "Zec"
            "malachi" -> "Mal"
            "matthew" -> "Mat"
            "the gospel according to matthew" -> "Mat"
            "mark" -> "Mar"
            "luke" -> "Luk"
            "john" -> "Joh"
            "the gospel according to john" -> "Joh"
            "acts" -> "Act"
            "romans" -> "Rom"
            "1 corinthians" -> "1Co"
            "2 corinthians" -> "2Co"
            "galatians" -> "Gal"
            "ephesians" -> "Eph"
            "philippians" -> "Phi"
            "colossians" -> "Col"
            "1 thessalonians" -> "1Th"
            "2 thessalonians" -> "2Th"
            "1 timothy" -> "1Ti"
            "2 timothy" -> "2Ti"
            "titus" -> "Tit"
            "philemon" -> "Phm"
            "hebrews" -> "Heb"
            "james" -> "Jam"
            "1 peter" -> "1Pe"
            "2 peter" -> "2Pe"
            "1 john" -> "1Jo"
            "2 john" -> "2Jo"
            "3 john" -> "3Jo"
            "jude" -> "Jud"
            "revelation" -> "Rev"
            else -> bookName.take(3).replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}