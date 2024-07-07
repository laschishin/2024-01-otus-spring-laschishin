async function queryJson(url, requestParams={}) {

    const response = await fetch(url, requestParams);

    if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
    }

    var json_data;

    try{
        json_data = await response.json();
    }
    catch (error) {
        throw new Error("Response has no json data");
    }

    if(json_data === null) {
        throw new Error("Response has no data");
    }

    return json_data;
}

function getAuthorsFullNames(authors) {
    return authors.flatMap(e => e.fullName).join(', ');
}
function getAuthorsIds(authors) {
    return authors.flatMap(e => e.id);
}

// Warn if overriding existing method
if(Array.prototype.equals)
    console.warn("Overriding existing Array.prototype.equals. Possible causes: New API defines the method, there's a framework conflict or you've got double inclusions in your code.");
// attach the .equals method to Array's prototype to call it on any array
Array.prototype.equals = function (array) {
    // if the other array is a falsy value, return
    if (!array)
        return false;
    // if the argument is the same array, we can be sure the contents are same as well
    if(array === this)
        return true;
    // compare lengths - can save a lot of time
    if (this.length != array.length)
        return false;

    for (var i = 0, l=this.length; i < l; i++) {
        // Check if we have nested arrays
        if (this[i] instanceof Array && array[i] instanceof Array) {
            // recurse into the nested arrays
            if (!this[i].equals(array[i]))
                return false;
        }
        else if (this[i] != array[i]) {
            // Warning - two different object instances will never be equal: {x:20} != {x:20}
            return false;
        }
    }
    return true;
}
// Hide method from for-in loops
Object.defineProperty(Array.prototype, "equals", {enumerable: false});



//async function fillBooksTable1() {
//
//    const url = "/api/v1/books";
//
//    try {
//
//        const response = await fetch(url);
//
//        if (!response.ok) {
//            throw new Error(`Response status: ${response.status}`);
//        }
//
//        var books;
//
//        try{
//            books = await response.json();
//        }
//        catch (error) {
//            throw new Error("Response has no json data");
//        }
//
//
//        if(books === null) {
//            throw new Error("Response has no entites");
//        }
//
//        books.forEach((book) => {
//
//            document.getElementById("books-table").tBodies[0].innerHTML += `
//    <tr>
//        <td>${book.id}</td>
//        <td>
//            <a href="/book/view/${book.id}" >${book.title}</a>
//        </td>
//        <td>${book.authorsFullNames}</td>
//        <td>${book.genre.name}</td>
//        <td>
//            <a href="/book/edit/${book.id}">Edit</a>
//        </td>
//        <td>
//            <a href="/book/delete/${book.id}">Delete</a>
//        </td>
//    </tr>
//`;
//        });
//        document.getElementById("error_box").hidden = true;
//
//    } catch (error) {
//        console.error(error.message);
//        document.getElementById("error_box").textContent = `Books fetch error: ${error.message}`
//        document.getElementById("error_box").hidden = false;
//    }
//};
//
//async function fillBooksTable2() {
//    const booksTable = document.querySelector("#books-table tbody");
//    const myRequest = new Request("/api/v1/books");
//
//    fetch(myRequest)
//        .then((response) => response.json())
//        .then((data) => {
//        for (const book of data) {
//            booksTable.innerHTML += `
//    <tr>
//        <td>${book.id}</td>
//        <td>
//            <a href="/book/view/${book.id}" >${book.title}</a>
//        </td>
//        <td>${book.authorsFullNames}</td>
//        <td>${book.genre.name}</td>
//        <td>
//            <a href="/book/edit/${book.id}">Edit</a>
//        </td>
//        <td>
//            <a href="/book/delete/${book.id}">Delete</a>
//        </td>
//    </tr>
//`;
//        }
//    })
//        .catch ((error) => {
//        console.error(error.message);
//        document.getElementById("error_box").textContent = `Books fetch error: ${error.message}`
//        document.getElementById("error_box").hidden = false;
//    });
//
//}