[![](war/images/booklion.png)](https://booklion.com)


# BookLion

## Book Summaries You Can Edit

BookLion is a web-based, publicly accessible ([https://booklion.com](https://booklion.com)) system designed to allow the user to store
the following information about a book they are reading:

- Book title, author, year, genre, and description
- Chapter summaries
- Character descriptions
- Locations
- Species

The system allows book summaries to be shared among all users of the system so that if
one person enters information about a book, other users of the system can view and edit 
notes on all books.

The system also tracks the chapter with which each note is associated.  This way, other
users can limit what they see and avoid unwanted spoilers.

### Technology

BookLion is written in Java utilizing GWT and SQL.

While this software runs as intended very well, it is an utter embarrassment in terms 
of the technology chosen for its development.  It is the first web-based application I wrote from scratch by myself.
Not fully understanding the landscape, wanting to avoid HTML, and falling for Google's hype,
I chose the Google Web Toolkit (GWT).  Also, in ignorance, I chose the Hibernate ORM.  Both
turned out to be extremely poor choices.  Yet, the system runs fine, and I've learned some
valuable lessons.

This system requires:

- Java 7
- GWT 2.5.1 (included)
- Tomcat 7
- Hibernate 3.6.5 (included)

As developed, the system uses the PostgreSQL database.  The schema is located in
```schema/booklion-schema.sql```

An IntelliJ project is included with the system.

Subsequent to the development of this system, and perhaps in response to the experience,
I ended up writing my own open-source, full-stack web development framework called KISS.
You can check it out at [https://kissweb.org](https://kissweb.org) or [https://github.com/blakemcbride/Kiss](https://github.com/blakemcbride/Kiss)

The home for the code is at [https://github.com/blakemcbride/BookLion](https://github.com/blakemcbride/BookLion)
