package booklion.server.login;

import utils.DateUtils;
import utils.EMail;

/**
 * @author Blake McBride
 *
 * Date: 4/9/14
 */
public class LoginEmailNotification {

    public static void send(String name, String email, String login, String password) {
        String body = "  <style type='text/css'>" +
                "    .login { font-size: 1em; color: blue;  }" +
                "  </style>" +

                "<h2>Welcome to BookLion</h2>" +
                "<p>Thank you for your interest in BookLion.  Your sign-on information is contained in this email.</p>" +
                "<p>By signing on to BookLion, you are agreeing to our terms of use." +
                "  Please click <a href='http://booklion.com/TermsOfUse.html'>here</a> to see those terms.</p><br/>" +

                "<p>URL:  &nbsp; <a href='http://booklion.com'>booklion.com</a> </p>" +

                "<p>Login ID: &nbsp;  <span class='login'>" + login + "</span></p>" +
                "<p>Password: &nbsp;  <span class='login'>" + password + "</span></p><br/>" +

                "<p>BookLion is an Internet Web Application for keeping and sharing detailed information about books.  The details provided by BookLion are far more structured and detailed than those provided by other book summaries, or overviews.  BookLion mainly solves the following challenges:</p>" +

                "<ol><li>Did you ever read the first six chapters of a book,  put it down for a week or more because of some distraction or other obligation?  When you are finally able to get back to the book, you find you do not remember all of the characters or the plot details anymore.  You may skim backwards, figure you will pick it up as you go, or worse yet, you drop the book altogether.  None of these options give you back what you lost when you temporarily dropped the book.  Starting over is rarely an option. </li><br/>" +

                "<li>When reading a book, it will often have too many characters or locations of which to keep track.  In chapter twelve the author may bring up \"Sam\" who was last mentioned in chapter three.  Or, perhaps there are 22 characters related in three family groups.  It would also be hard to keep track of six locations and eight characters who travel in two groups to various locations.</li></ol>" +

                "<p>These sorts of situations are common and very difficult to track.  We find ourselves glossing over the more minor characters and locations attempting to get the gist of the story. </p>" +

                "<p>BookLion solves these problems and more! </p>" +

                "<p>Imagine being able to keep all of the characters, locations, and species (fantasy and science fiction) straight.  You would get more out of books and enjoy them more.  Imagine having the plot detailed for each chapter.  BookLion provides this level of detail.</p>" +

                "<p>Another big benefit of BookLion is the ability to restrict what BookLion tells you.   You can avoid spoilers.  This works as follows: </p>" +

                "<p>Imagine you put your book down for two weeks after reading nine chapters.  You know where you stopped because there is a bookmark in the book, but you do not remember all of the details.  You go to BookLion, and see that other people have entered information about that book.  You are able to tell BookLion that you only read up to chapter nine.  BookLion will only give you information up to chapter nine.  All of the characters, locations, and chapter plots that occur after that point will be hidden from you.  You get the full details without spoilers!</p>" +

                "<p>BookLion keeps detailed information about a book including plot threads by chapter, characters, locations, and species (good for fantasy and science fiction books).  When you add new data about a book, BookLion asks you in what chapter that information was introduced. </p>" +

                "<p>BookLion provides far greater detail than widely-available book summaries.  This would be an extremely valuable tool for students.</p>";


        EMail.send("BookLion <do-not-reply@booklion.com>", name + " <" + email + ">", "BookLion Login Information " + DateUtils.getCurrentTimeStamp(), body);
    }

}
