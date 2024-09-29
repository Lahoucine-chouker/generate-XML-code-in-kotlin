import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.OutputKeys

fun main() {
    //Changed 'Chouk' to your user.
    val filePath = "C:/Users/chouk/Documents/users.xml"

    if (!File(filePath).exists()) {
        createInitialXmlFile(filePath)
    }

    while (true) {
        println("Choose an option:")
        println("1. View stagiers")
        println("2. Add a new stagier")
        println("3. Exit")

        when (readLine()?.trim()) {
            "1" -> {
                val document = parseXml(filePath)
                displayXmlContent(document)
            }
            "2" -> {
                val document = parseXml(filePath)
                enterStagierInfo(document)
                saveXmlToFile(document, filePath)
            }
            "3" -> {
                println("Exiting...")
                return
            }
            else -> println("Invalid option. Please try again.")
        }
    }
}

fun createInitialXmlFile(filePath: String) {
    val xmlContent = """
        <?xml version="1.0" encoding="UTF-8"?>
        <stagiers>
        </stagiers>
    """.trimIndent()
    val file = File(filePath)
    file.writeText(xmlContent)
    println("Initial XML file created at ${file.absolutePath}")
}


fun enterStagierInfo(document: Document) {
    val stagierId = document.getElementsByTagName("stagier").length + 1

    println("Enter information for the new stagier:")
    print("Name: ")
    val name = readLine() ?: ""
    print("Prenom: ")
    val prenom = readLine() ?: ""
    print("Age: ")
    val age = readLine() ?: ""
    print("Email: ")
    val email = readLine() ?: ""
    print("Date de Naissance (YYYY-MM-DD): ")
    val dateDeNaissance = readLine() ?: ""
    print("Group: ")
    val group = readLine() ?: ""
    print("Filier: ")
    val filier = readLine() ?: ""
    print("Annes d'Etudiant: ")
    val annesDetudiant = readLine() ?: ""

    val newStagier = document.createElement("stagier").apply {
        setAttribute("id", stagierId.toString())
        appendChild(createElementWithText(document, "name", name))
        appendChild(createElementWithText(document, "prenom", prenom))
        appendChild(createElementWithText(document, "age", age))
        appendChild(createElementWithText(document, "email", email))
        appendChild(createElementWithText(document, "date_de_naissance", dateDeNaissance))
        appendChild(createElementWithText(document, "group", group))
        appendChild(createElementWithText(document, "filier", filier))
        appendChild(createElementWithText(document, "annes_detudiant", annesDetudiant))
    }

    document.documentElement.appendChild(newStagier)
    println("New stagier added successfully!")
}

// Helper function to create an element with text content
fun createElementWithText(doc: Document, tagName: String, textContent: String): org.w3c.dom.Element {
    val element = doc.createElement(tagName)
    element.textContent = textContent
    return element
}

fun saveXmlToFile(document: Document, filePath: String) {
    val transformer = TransformerFactory.newInstance().newTransformer().apply {
        setOutputProperty(OutputKeys.INDENT, "yes")
        setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")  // Use 4 spaces for indentation
    }
    val domSource = DOMSource(document)
    val streamResult = StreamResult(File(filePath))
    transformer.transform(domSource, streamResult)
    println("Updated XML file saved at $filePath")
}


fun parseXml(path: String): Document {
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()
    val documentBuilder = documentBuilderFactory.newDocumentBuilder()
    return documentBuilder.parse(File(path))
}

fun displayXmlContent(document: Document) {
    val root = document.documentElement
    println("Root Element: ${root.nodeName}")

    val nodeList = root.childNodes
    for (i in 0 until nodeList.length) {
        val node = nodeList.item(i)
        if (node.nodeType == Document.ELEMENT_NODE) {
            val element = node as org.w3c.dom.Element
            println("Tag: ${element.tagName}")
            val attributes = element.attributes
            for (j in 0 until attributes.length) {
                val attribute = attributes.item(j)
                println("Attribute: ${attribute.nodeName} = ${attribute.nodeValue}")
            }
            println("Name: ${element.getElementsByTagName("name").item(0).textContent}")
            println("Prenom: ${element.getElementsByTagName("prenom").item(0).textContent}")
            println("Age: ${element.getElementsByTagName("age").item(0).textContent}")
            println("Email: ${element.getElementsByTagName("email").item(0).textContent}")
            println("Date de Naissance: ${element.getElementsByTagName("date_de_naissance").item(0).textContent}")
            println("Group: ${element.getElementsByTagName("group").item(0).textContent}")
            println("Filier: ${element.getElementsByTagName("filier").item(0).textContent}")
            println("Annes d'Etudiant: ${element.getElementsByTagName("annes_detudiant").item(0).textContent}")
            println()  // Blank line between stagiers for readability
        }
    }
}
