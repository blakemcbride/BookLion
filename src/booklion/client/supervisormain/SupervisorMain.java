package booklion.client.supervisormain;

import booklion.client.booklist.Booklist;
import booklion.client.bookmain.BookMain;
import booklion.client.chapteredit.ChapterList;
import booklion.client.characteredit.CharacterList;
import booklion.client.edituser.EditUser;
import booklion.client.genre.GenreMaint;
import booklion.client.global.Information;
import booklion.client.locationedit.LocationList;
import booklion.client.login.LoginService;
import booklion.client.speciesedit.SpeciesList;
import booklion.client.userInfoChange.UserInfoChange;
import booklion.client.userLoginChange.UserLoginChange;
import booklion.client.utils.*;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;


public class SupervisorMain implements EntryPoint {

	private static final String userMaint = "userMaint";
	private static final String genreMaint = "genreMaint";
    private static final String bookList = "bookList";
    private static final String bookMain = "bookMain";
    private static final String bookAdd = "bookAdd";
    private static final String bookEdit= "bookEdit";
    private static final String bookDelete = "bookDelete";
    private static final String bookSearch = "bookSearch";
    private static final String bookShowAll = "bookShowAll";
    private static final String bookSelect = "bookSelect";
	private static final String logout = "logout";
    private static final String support = "support";
    private static final String chapterList = "chapterList";
    private static final String characterList = "characterList";
    private static final String locationList = "locationList";
    private static final String speciesList = "speciesList";
    private static final String userInfoChange = "userInfoChange";
    private static final String userLoginChange = "userLoginChange";
	private static DockLayoutPanelEx dataPanel;
    private static MenuBarEx mainMenu;
    private static DockLayoutPanel topPanel;
    private static InlineHTML headerHtml;
    private static boolean inBookList = false;
    private static Booklist bookListIns;
    private static LocalSelectionHandler handler;

	public void onModuleLoad() {
        RootLayoutPanel rlp = RootLayoutPanel.get();
        rlp.clear();
        topPanel = new DockLayoutPanel(Unit.EM);
        rlp.add(topPanel);

        Message.setBackButtonMessage("Hitting the back button will exit BookLion and go back to the previous web site you were at.  To navigate within BookLion stay on this page and click on the tag specifying what you want to do.");

        inBookList = false;

        HorizontalPanel hp = new HorizontalPanel();

        SimplePanel sp = new SimplePanel();
        sp.setSize(".5in", "1in");
        hp.add(sp);


        hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Image logo = new Image("images/smalllion.png");
        logo.setSize(".75in", ".75in");
        hp.add(logo);

        setBookHeader();

//        Label lbl = new Label("BookLion - Main Menu");
        hp.add(headerHtml);
//        hp.setVerticalAlignment();


//        topPanel.addNorth(new Label("BookLion - Main Menu"), 2);
//        topPanel.addNorth(hp, 7);
        topPanel.addNorth(hp, 7);


        MenuBarEx bar = updateMenu();
        topPanel.addNorth(bar, 2);
        dataPanel = new DockLayoutPanelEx(Unit.EM);
        topPanel.add(dataPanel);
        rlp.forceLayout();

        // when user first loggs on, display the book list
        Scheduler.get().scheduleDeferred(new Command() {
            public void execute () {
                handler.executeRequest(bookList);  // show book list on entry
            }
        });

    }

    public static void setBookHeader() {
        String book = Information.getBookTitle();
        String chapterLimit = Information.getChapterLimitName();
        String title = "<span style=\"font-size:xx-large\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;BookLion</span>";
        if (book != null  &&  !book.isEmpty()) {
            title += "<span style=\"font-size:x-large\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + book + "</span>";
            if (chapterLimit != null  &&  !chapterLimit.isEmpty())
                title += "<span style=\"font-size:large\">&nbsp;&nbsp;&nbsp;&nbsp;(" + chapterLimit + ")</span>";
        }
        if (headerHtml == null)
            headerHtml = new InlineHTML(title);
        else
            headerHtml.setHTML(title);
    }

    public static void forceLayout() {
        topPanel.forceLayout();
    }

    public static DockLayoutPanelEx getPanel() {
        return dataPanel;
    }

    public static MenuBarEx updateMenu() {
        MenuEx menu;
        if (mainMenu == null)
            mainMenu = new MenuBarEx();
        else
            mainMenu.clear();
        handler = new LocalSelectionHandler();

        mainMenu.setBorders(false);

        // ///////////////

        if (!Information.isGuest()) {
            menu = mainMenu.addMenuBarItem("Administrative");
            if (Information.isSupervisor()) {
                menu.addMenuItem("Users", userMaint, handler);
                menu.addMenuItem("Book Genre", genreMaint, handler);
                menu.addMenuItem("Account Info", userInfoChange, handler);
                menu.addMenuItem("Email / Login", userLoginChange, handler);
            } else {
                menu.addMenuItem("Account Info", userInfoChange, handler);
                menu.addMenuItem("Email / Login", userLoginChange, handler);
            }
        }

        // ///////////////

        mainMenu.addMenuBarDirectItem(bookList, new MenuBarItemClickOnly("Book List", handler));

        if (Information.getBook() != null) {
            mainMenu.addMenuBarDirectItem(bookMain, new MenuBarItemClickOnly("Book Main", handler));
            mainMenu.addMenuBarDirectItem(chapterList, new MenuBarItemClickOnly("Chapters", handler));
            mainMenu.addMenuBarDirectItem(characterList, new MenuBarItemClickOnly("Characters", handler));
            mainMenu.addMenuBarDirectItem(locationList, new MenuBarItemClickOnly("Locations", handler));
            mainMenu.addMenuBarDirectItem(speciesList, new MenuBarItemClickOnly("Species", handler));
        } else if (inBookList) {
            mainMenu.addMenuBarDirectItem(bookSelect, new MenuBarItemClickOnly("Go Into Selected Book", handler));
            mainMenu.addMenuBarDirectItem(bookAdd, new MenuBarItemClickOnly("Add New Book", handler));
      //      mainMenu.addMenuBarDirectItem(bookEdit, new MenuBarItemClickOnly("Edit Book ", handler));
            mainMenu.addMenuBarDirectItem(bookDelete, new MenuBarItemClickOnly("Delete Book ", handler));
            mainMenu.addMenuBarDirectItem(bookSearch, new MenuBarItemClickOnly("Search ", handler));
            mainMenu.addMenuBarDirectItem(bookShowAll, new MenuBarItemClickOnly("Show All ", handler));
        }



        // ///////////////

        mainMenu.addMenuBarDirectItem(support, new MenuBarItemClickOnly("Support", handler));
        mainMenu.addMenuBarDirectItem(logout, new MenuBarItemClickOnly("Logout", handler));

        // //////////////////

//		bar.getElement().getStyle().setProperty("backgroundImage", "none");
//		bar.getElement().getStyle().setProperty("backgroundColor", Settings.backgroundColor);
//        bar.getElement().getStyle().setProperty("color", Settings.menuColor);

        return mainMenu;
    }

    private static boolean editUserLoaded = false;
    private static boolean genreMaintLoaded = false;
    private static boolean bookListLoaded = false;
    private static boolean bookMainLoaded = false;
    private static boolean chapterListLoaded = false;
    private static boolean characterListLoaded = false;
    private static boolean locationListLoaded = false;
    private static boolean speciesListLoaded = false;
    private static boolean userInfoLoaded = false;
    private static boolean userLoginLoaded = false;

    private static class LocalSelectionHandler extends MenuSelectionHandler {

        private static final String failedDownload = "Code download failed.  Possible Internet connection problem.";
        private static final String leaveOK = "Unsaved data exists; Leave without saving?";
		
		public void executeRequest(String selection) {
			if (selection == userMaint) {
                inBookList = false;
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!editUserLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                editUserLoaded = Message.loadDone();
                                new EditUser().onModuleLoad();
                            }
                        });
                    }
                };

            } else if (selection == logout) {
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        Message.deleteBackButtonMessage();
                        LoginService.Util.getInstance().logout(new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
//                                new Login().onModuleLoad();
                                Window.Location.reload();
                            }

                            @Override
                            public void onSuccess(Void result) {
//                                new Login().onModuleLoad();
                                Window.Location.reload();
                            }
                        });
                    }
                };

            } else if (selection == support) {
                Support.support();
            } else if (selection == genreMaint) {
                inBookList = false;
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!genreMaintLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                genreMaintLoaded = Message.loadDone();
                                new GenreMaint().onModuleLoad();
                            }
                        });

                    }
                };

            } else if (selection == bookList) {
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!bookListLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                inBookList = true;
                                bookListLoaded = Message.loadDone();
                                (bookListIns=new Booklist()).onModuleLoad();
                            }
                        });
                    }
                };

            } else if (selection == bookMain) {
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (Information.getBook() == null)
                            Message.msgOk("Please select book through Book List first.");
                        else {

                            if (!bookMainLoaded)
                                Message.loadingMsg();
                            GWT.runAsync(new RunAsyncCallback() {

                                @Override
                                public void onFailure(Throwable reason) {
                                    Message.loadDone();
                                    Message.systemError(failedDownload);
                                }

                                @Override
                                public void onSuccess() {
                                    bookMainLoaded = Message.loadDone();
                                    new BookMain().onModuleLoad();
                                }
                            });
                        }
                    }
                };

            } else if (selection == chapterList) {
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!chapterListLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                chapterListLoaded = Message.loadDone();
                                new ChapterList().onModuleLoad();
                            }
                        });
                    }
                };

            } else if (selection == characterList) {
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!characterListLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                characterListLoaded = Message.loadDone();
                                new CharacterList().onModuleLoad();
                            }
                        });
                    }
                };

            } else if (selection == locationList) {
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!locationListLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                locationListLoaded = Message.loadDone();
                                new LocationList().onModuleLoad();
                            }
                        });

                    }
                };

            } else if (selection == speciesList) {
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!speciesListLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                speciesListLoaded = Message.loadDone();
                                new SpeciesList().onModuleLoad();
                            }
                        });

                    }
                };

            } else if (selection == bookAdd) {
                if (Message.notForGuest())
                    return;
                //  This code will already have been loaded but this indirection is necessary to prevent pre-use loading.
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        Message.systemError(failedDownload);
                    }

                    @Override
                    public void onSuccess() {
                        bookListIns.bookAdd();
                    }
                });
            } else if (selection == bookEdit) {
                if (Message.notForGuest())
                    return;
                //  This code will already have been loaded but this indirection is necessary to prevent pre-use loading.
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        Message.systemError(failedDownload);
                    }

                    @Override
                    public void onSuccess() {
                        bookListIns.bookEdit();
                    }
                });

            } else if (selection == bookDelete) {
                if (Message.notForGuest())
                    return;
                //  This code will already have been loaded but this indirection is necessary to prevent pre-use loading.
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        Message.systemError(failedDownload);
                    }

                    @Override
                    public void onSuccess() {
                        bookListIns.bookDelete();
                    }
                });

            } else if (selection == bookSearch) {
                //  This code will already have been loaded but this indirection is necessary to prevent pre-use loading.
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        Message.systemError(failedDownload);
                    }

                    @Override
                    public void onSuccess() {
                        bookListIns.bookSearch();
                    }
                });

            } else if (selection == bookShowAll) {
                //  This code will already have been loaded but this indirection is necessary to prevent pre-use loading.
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        Message.systemError(failedDownload);
                    }

                    @Override
                    public void onSuccess() {
                        bookListIns.bookShowAll();
                    }
                });

            } else if (selection == bookSelect) {
                //  This code will already have been loaded but this indirection is necessary to prevent pre-use loading.
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        Message.systemError(failedDownload);
                    }

                    @Override
                    public void onSuccess() {
                        bookListIns.bookSelect();
                    }
                });

            } else if (selection == userInfoChange) {
                inBookList = false;
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!userInfoLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                userInfoLoaded = Message.loadDone();
                                new UserInfoChange().onModuleLoad();
                            }
                        });
                    }
                };

            } else if (selection == userLoginChange) {
                inBookList = false;
                new ExecuteIfNotDirty(leaveOK) {
                    @Override
                    public void execute() {
                        if (!userLoginLoaded)
                            Message.loadingMsg();
                        GWT.runAsync(new RunAsyncCallback() {

                            @Override
                            public void onFailure(Throwable reason) {
                                Message.loadDone();
                                Message.systemError(failedDownload);
                            }

                            @Override
                            public void onSuccess() {
                                userLoginLoaded = Message.loadDone();
                                new UserLoginChange().onModuleLoad();
                            }
                        });
                    }
                };

            }
        }

	}

    public static void setInBookList(boolean inBookList) {
        SupervisorMain.inBookList = inBookList;
    }
}
