var originalBook = {};
var originalAuthors = {};
var originalGenres = {};


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

            originalAuthors = result;

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

            originalGenres = result;

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

    queryJson(`/api/v1/books/${bookId}`)
        .then( (result) => {

        originalBook = result;
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


function putBookEdit() {

    var authors = [];
    for(let item of document.querySelectorAll("#book-authors option[selected]")) {
        authors.push({
            id: item.value,
            fullName: ""
        });
    };
    var genre= {
        id: document.querySelector("#book-genre [selected]").value,
        name: ""
    };

    var book = {};

    book.id = document.querySelector("#book-id").value;
    book.title = document.querySelector("#book-title").value;
    book.authors = authors;
    book.genre = genre;



    const requestParams = {
        method: "PUT",
        body: JSON.stringify({book: book})
    };

    queryJson(`/api/v1/books/${book.id}`, requestParams)
        .then( (result) => {
            const book = result;
//            document.querySelector("#book-id").value = book.id;
//            document.querySelector("#book-title").value = book.title;
//            populateAuthors(book.authorsIds);
//            populateGenres(book.genre.id);
//            document.getElementById("error_box").hidden = true;
            window.location.replace("/");
        }).catch ((error) => {
            console.error(error.message);
            document.getElementById("error_box").textContent = `Book update error: ${error.message}`
            document.getElementById("error_box").hidden = false;
        });



}