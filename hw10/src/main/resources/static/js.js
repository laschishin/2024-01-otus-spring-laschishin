async function queryJson(url) {

    const response = await fetch(url);

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
        throw new Error("Response has no entites");
    }

    return json_data;
}

function fillBookList() {

    var tableContainer = document.querySelector("#books-table tbody");
    queryJson("/api/v1/books")
        .then( (result) => {

            result.forEach((book) => {

                tableContainer.innerHTML += `
                <tr>
                    <td>${book.id}</td>
                    <td>
                        <a href="/book/view/${book.id}" >${book.title}</a>
                    </td>
                    <td>${book.authorsFullNames}</td>
                    <td>${book.genre.name}</td>
                    <td>
                        <a href="/book/edit/${book.id}">Edit</a>
                    </td>
                    <td>
                        <a href="/book/delete/${book.id}">Delete</a>
                    </td>
                </tr>`;
            });
            document.getElementById("error_box").hidden = true;
        })
        .catch ((error) => {
            console.error(error.message);
            document.getElementById("error_box").textContent = `Books fetch error: ${error.message}`
            document.getElementById("error_box").hidden = false;
        });
}

function fillBookView(bookId) {

    queryJson(`/api/v1/books/${bookId}`)
        .then( (result) => {

            const book = result;
            document.querySelector("#book-id").innerHTML = book.id;
            document.querySelector("#book-title").innerHTML = book.title;
            document.querySelector("#book-authors").innerHTML = book.authorsFullNames;
            document.querySelector("#book-genre").innerHTML = book.genre.name;
            document.querySelector("#book-edit-link").href = `/book/edit/${book.id}`;
            document.getElementById("error_box").hidden = true;
        })
        .catch ((error) => {
            console.error(error.message);
            document.getElementById("error_box").textContent = `Book fetch error: ${error.message}`
            document.getElementById("error_box").hidden = false;
        });


    var bookCommentsContainer = document.querySelector("#book-comments");
    queryJson(`/api/v1/books/${bookId}/comments`)
        .then( (result) => {

        result.forEach((bookComment) => {

            bookCommentsContainer.innerHTML += `<div class="book_comment"><span>${bookComment.textContent}</span></div>`;
        });
        document.getElementById("error_box").hidden = true;
    })
        .catch ((error) => {
        console.error(error.message);
        document.getElementById("error_box").textContent = `Book fetch error: ${error.message}`
        document.getElementById("error_box").hidden = false;
    });

}

function fillBookEdit(bookId) {

    function populateAuthors(bookAuthors) {
        var bookAuthorsContainer = document.querySelector("#book-authors");
        queryJson("/api/v1/authors")
            .then( (result) => {

            result.forEach((author) => {

                bookAuthorsContainer.innerHTML += `
                <option value="${author.id}" ${bookAuthors.includes(author.id) ? "selected" : ""}>${author.fullName}</option>
            `;
            });
            document.getElementById("error_box").hidden = true;
        })
            .catch ((error) => {
            console.error(error.message);
            document.getElementById("error_box").textContent = `Book authors fetch error: ${error.message}`
            document.getElementById("error_box").hidden = false;
        });
    }

    function populateGenres(bookGenre) {
        var bookGenresContainer = document.querySelector("#book-genre");
        queryJson("/api/v1/genres")
            .then( (result) => {

            result.forEach((genre) => {

                bookGenresContainer.innerHTML += `
                <option value="${genre.id}" ${bookGenre == genre.id ? "selected" : ""}>${genre.name}</option>
            `;
            });
            document.getElementById("error_box").hidden = true;
        })
            .catch ((error) => {
            console.error(error.message);
            document.getElementById("error_box").textContent = `Book genres fetch error: ${error.message}`
            document.getElementById("error_box").hidden = false;
        });
    }

    var bookAuthors;
    queryJson(`/api/v1/books/${bookId}`)
        .then( (result) => {

        const book = result;
        document.querySelector("#book-id").value = book.id;
        document.querySelector("#book-title").value = book.title;
        populateAuthors(book.authorsIds);
        populateGenres(book.genre.id);
        document.getElementById("error_box").hidden = true;
    })
        .catch ((error) => {
        console.error(error.message);
        document.getElementById("error_box").textContent = `Book fetch error: ${error.message}`
        document.getElementById("error_box").hidden = false;
    });




}

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