var originalBook = {};
var originalAuthors = {};
var originalGenres = {};


function fillBookView(bookId) {

    queryJson(`/api/v1/books/${bookId}`)
        .then( (result) => {

            const book = result;
            document.querySelector("#book-id").innerHTML = book.id;
            document.querySelector("#book-title").innerHTML = book.title;
            document.querySelector("#book-authors").innerHTML = getAuthorsFullNames(book.authors);
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
        populateAuthors(getAuthorsIds(book.authors));
        populateGenres(book.genre.id);
        document.getElementById("error_box").hidden = true;
    })
        .catch ((error) => {
        console.error(error.message);
        document.getElementById("error_box").textContent = `Book fetch error: ${error.message}`
        document.getElementById("error_box").hidden = false;
    });

}

function prepareRequestBody(originalBook) {
    var book = {};

    for (const element of document.querySelector("form").elements) {
        switch(element.id) {
            case "book-title":
                let formTitle = element.value;
                if (formTitle.localeCompare(originalBook.title) != 0) {
                    book.title = formTitle;
                }
                break;
            case "book-authors":
                let formAuthors = Array.from(element.selectedOptions).flatMap(e => e.value).sort();
                let origAuthors = originalBook.authors.flatMap(e => e.id).sort();
                if (formAuthors.equals(origAuthors) == false) {
                    book.authors = formAuthors;
                }
                break;
            case "book-genre":
                let formGenre = element.selectedOptions[0].value;
                if (formGenre.localeCompare(originalBook.genre.id) != 0) {
                    book.genre = formGenre;
                }
                break;
        }
    }

//    let title = document.querySelector("#book-title").value;
//    if (title.localeCompare(originalBook.title) != 0) {
//        book.title = title;
//    }
//
//    let authors = [];
//    for(let item of document.querySelector("#book-authors").selectedOptions) {
//        authors.push(item.value);
//    };
//    authors = authors
//        .sort();
//    let bookAuthorsId = originalBook.authors
//        .flatMap(e => e.id)
//        .sort();
////    if (JSON.stringify(authors.sort()).localeCompare(JSON.stringify(bookAuthorsId)) != 0) {
//    if (authors.equals(bookAuthorsId) == false) {
//        book.authors = authors;
//    }
//
//    let genre = document.querySelector("#book-genre").selectedOptions[0].value;
//    if (genre.localeCompare(originalBook.genre.id) != 0) {
//        book.genre = genre;
//    }

    return book;
}

function submitEditBook() {


    bookId = document.querySelector("#book-id").value;

    requestBody = prepareRequestBody(originalBook);

    if(Object.keys(requestBody).length == 0) {
        document.getElementById("error_box").textContent = "There is no changes";
        document.getElementById("error_box").hidden = false;
        return;
    }

    const requestParams = {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody)
    };

    queryJson(`/api/v1/books/${bookId}`, requestParams)
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