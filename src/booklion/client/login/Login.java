package booklion.client.login;

import booklion.client.global.Information;
import booklion.client.supervisormain.SupervisorMain;

import booklion.client.utils.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;


import static booklion.client.utils.ControlUtils.setInitialFocus;

public class Login implements EntryPoint {

	private LoginServiceAsync loginService = LoginService.Util.getInstance();
	
//	private Button loginButton;
//	private TextBox loginIDField;
//	private TextBox passwordField;
	
	private TextField loginIDField;
	private PasswordField passwordField;
	private TextButton loginButton;

    private String part1() {
        return "th";
    }
	
//	private boolean test() {
//		RootLayoutPanel root = RootLayoutPanel.get();
//
//		Button topLeft = new Button("Top Left");
//		root.add(topLeft);
//		root.setWidgetLeftWidth(topLeft, 0, Unit.PX, 50, Unit.PCT);
//		root.setWidgetTopHeight(topLeft, 0, Unit.PX, 50, Unit.PCT);
//		root.setWidgetHorizontalPosition(topLeft, Alignment.BEGIN);
//		root.setWidgetVerticalPosition(topLeft, Alignment.BEGIN);
//
//		Button topRight = new Button("Top Right");
//		root.add(topRight);
//		root.setWidgetRightWidth(topRight, 0, Unit.PX, 50, Unit.PCT);
//		root.setWidgetTopHeight(topRight, 0, Unit.PX, 50, Unit.PCT);
//		root.setWidgetHorizontalPosition(topRight, Alignment.END);
//		root.setWidgetVerticalPosition(topRight, Alignment.BEGIN);
//
//		Button bottomLeft = new Button("Bottom Left");
//		root.add(bottomLeft);
//		root.setWidgetLeftWidth(bottomLeft, 0, Unit.PX, 50, Unit.PCT);
//		root.setWidgetBottomHeight(bottomLeft, 0, Unit.PX, 50, Unit.PCT);
//		root.setWidgetHorizontalPosition(bottomLeft, Alignment.BEGIN);
//		root.setWidgetVerticalPosition(bottomLeft, Alignment.END);
//
//		Button bottomRight = new Button("Bottom Right");
//		root.add(bottomRight);
//		root.setWidgetRightWidth(bottomRight, 0, Unit.PX, 50, Unit.PCT);
//		root.setWidgetBottomHeight(bottomRight, 0, Unit.PX, 50, Unit.PCT);
//		root.setWidgetHorizontalPosition(bottomRight, Alignment.END);
//		root.setWidgetVerticalPosition(bottomRight, Alignment.END);
//
//		return true;
//	}
	
	/*
	 * Displays two buttons natural height, max width
	 */
//	private void test2() {
//		RootLayoutPanel root = RootLayoutPanel.get();
//		NorthSouthContainer nsc = new NorthSouthContainer();
//		TextButton btn = new TextButton("Top Button");
////		btn.setLayoutData(new VerticalLayoutData(1, 1));
//		nsc.setNorthWidget(btn);
//		btn = new TextButton("Bottom Button");
////		btn.setLayoutData(new VerticalLayoutData(1, -1));
//		nsc.setSouthWidget(btn);
//		root.add(nsc);
//	}
	
	/*
	 * Displays two buttons max width.  Top button takes up most of the vertical space.  Bottom button takes up natural space.
	 */
//	private void test3() {
//		RootLayoutPanel root = RootLayoutPanel.get();
//		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
//		vlc.add(new TextButton("Top Button"), new VerticalLayoutData(1, .5));
//		vlc.add(new TextButton("Bottom button"), new VerticalLayoutData(1, .5));
//		root.add(vlc);
//	}
	
	// old one
//	public void onModuleLoad() {
////test6();
////if (true)
////	return;
//		RootPanel rootPanel = RootPanel.get();
//		rootPanel.clear();
//
//		rootPanel.getElement().getStyle().setProperty("backgroundColor", Settings.backgroundColor);
//		
//		uuid = null;
//		loginIDField = new TextBox();
//		rootPanel.add(loginIDField, 141, 112);
//
//		passwordField = new PasswordTextBox();
//		passwordField.addKeyPressHandler(new KeyPressHandler() {
//			public void onKeyPress(KeyPressEvent event) {
//				if (0 == (int) event.getCharCode())
//					attemptLogin();			
//			}
//		});
//		rootPanel.add(passwordField, 141, 146);
//		
//				loginButton = new Button();
//				rootPanel.add(loginButton, 191, 190);
//				loginButton.setText("Login");
//				loginButton.addClickHandler(new ClickHandler(){
//					public void onClick(ClickEvent event) {
//						attemptLogin();
//					}
//				});
//		
////		InlineHTML nlnhtmlnewInlinehtml = new InlineHTML("<center><h1>Welcome To BookLion</h1>\n<br>Find & keep detailed notes on books</center>\n");
////		rootPanel.add(nlnhtmlnewInlinehtml, 85, 21);
////		nlnhtmlnewInlinehtml.setSize("279px", "85px");
//		
//		Label lblLoginIdemail = new Label("Login ID:");
//		rootPanel.add(lblLoginIdemail, 81, 116);
//		
//		Label lblPassword = new Label("Password:");
//		rootPanel.add(lblPassword, 72, 150);
//		
//		HTML htmlNewHtml = new HTML("\u00A9 2011-2013 BookLion, LLC", true);
//		rootPanel.add(htmlNewHtml, 10, 275);
//		
//		Button moreInformationButton = new Button("More Information");
//		moreInformationButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				Message.systemError("Information goes here.");
//			}
//		});
//		rootPanel.add(moreInformationButton, 32, 190);
//		
//		Button newLoginButton = new Button("New Login ID");
//		newLoginButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				Message.systemError("Add a new user goes here.");
//			}
//		});
//		rootPanel.add(newLoginButton, 292, 190);
//		
//		Label lblNewLabel = new Label("(Email address)\n");
//		rootPanel.add(lblNewLabel, 284, 116);
//		
//		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//			   @Override
//			   public void execute() {
//				   loginIDField.setInitialFocus(true);
//			   }
//			});
//	}
	
	// causes unnecessary wrapping
//	private void test4() {
//		RootLayoutPanel rootPanel = RootLayoutPanel.get();
//		rootPanel.clear();
//		CenterLayoutContainer clc = new CenterLayoutContainer();
//		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
//
//		hlc.add(new Label("aaaaaaa aaaaaaa"), new HorizontalLayoutData(-1, -1, new Margins(0, 10, 0, 0)));
//		hlc.add(new Label("bbbbbbb bbbbbbb"), new HorizontalLayoutData(-1, -1, new Margins(0, 10, 0, 0)));
//		hlc.add(new Label("ccccccc ccccccc"), new HorizontalLayoutData(-1, -1, new Margins(0, 0, 0, 0)));
//
//		clc.add(hlc);
//		rootPanel.add(clc);
//	}

	//  works fine
//	private void test5() {
//		RootLayoutPanel rootPanel = RootLayoutPanel.get();
//		rootPanel.clear();
//
//		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
//
//		hlc.add(new Label("aaaaaaa aaaaaaa"), new HorizontalLayoutData(-1, -1, new Margins(0, 10, 0, 0)));
//		hlc.add(new Label("bbbbbbb bbbbbbb"), new HorizontalLayoutData(-1, -1, new Margins(0, 10, 0, 0)));
//		hlc.add(new Label("ccccccc ccccccc"), new HorizontalLayoutData(-1, -1, new Margins(0, 0, 0, 0)));
//
//		rootPanel.add(hlc);
//	}
	
	// causes unnecessary wrapping
//	private void test6() {
//		RootPanel.get().clear();
//		RootLayoutPanel rootPanel = RootLayoutPanel.get();
//		rootPanel.clear();
//		CenterLayoutContainer clc = new CenterLayoutContainer();
//		HBoxLayoutContainer hlc = new HBoxLayoutContainer();
//
//		hlc.add(new Label("aaaaaaa aaaaaaa"), new BoxLayoutData(new Margins(0, 10, 0, 0)));
//		hlc.add(new Label("bbbbbbb bbbbbbb"), new BoxLayoutData(new Margins(0, 10, 0, 0)));
//		hlc.add(new Label("ccccccc ccccccc"), new BoxLayoutData( new Margins(0, 0, 0, 0)));
//
//		clc.add(hlc);
//		rootPanel.add(clc);
//	}

//    private void test7() {
////        RootLayoutPanel root = RootLayoutPanel.get();
////        root.clear();
////        VerticalPanel vp = new VerticalPanel();
////        vp.add(new Label("AAAAAA"));
////        vp.add(new Label("BBBBBB"));
////        Label lbl = new Label("CCCCCC");
////        vp.add(lbl);
////        root.add(vp);
//
//
//
//
//
////        Widget childOne = new Label("AAAAAA"), childTwo = new Label("BBBBBB");
////        LayoutPanel p = new LayoutPanel();
////        p.add(childOne);
////        p.add(childTwo);
////
////        p.setWidgetLeftWidth(childOne, 0, Unit.PCT, 50, Unit.PCT);
////        p.setWidgetRightWidth(childTwo, 0, Unit.PCT, 50, Unit.PCT);
////
////        // Attach the LayoutPanel to the RootLayoutPanel. The latter will listen for
////        // resize events on the window to ensure that its children are informed of
////        // possible size changes.
////        RootLayoutPanel rp = RootLayoutPanel.get();
////        rp.add(p);
//
////        Widget childOne = new Label("AAAAAA"), childTwo = new Label("BBBBBB");
////        LayoutPanel p = RootLayoutPanel.get();
////        p.add(childOne);
////        p.add(childTwo);
////
////        p.setWidgetLeftWidth(childOne, 0, Unit.PCT, 50, Unit.PCT);
////        p.setWidgetRightWidth(childTwo, 0, Unit.PCT, 50, Unit.PCT);
//
//        Label childOne = new Label("AAAAAA"), childTwo = new Label("BBBBBB");
//        LayoutPanel p = RootLayoutPanel.get();
//
// //       p.setWidgetLeftWidth(childOne, 0, Unit.PCT, 90, Unit.PCT);
////        p.setWidgetRightWidth(childTwo, 0, Unit.PCT, 10, Unit.PCT);
// //       p.setWidgetHorizontalPosition(childTwo, Alignment.END);
// //       p.setWidgetVerticalPosition(childTwo, Alignment.END);
//        SimplePanel sp = new SimplePanel();
//        sp.add(childOne);
//        p.add(sp);
//        p.add(childTwo);
//
//        int w = sp.getOffsetHeight();
//        p.setWidgetTopHeight(childTwo, childOne.getOffsetHeight()*2, Unit.PX, 6, Unit.IN);
//        p.forceLayout();
//    }


    private TextBox fUsername;
    private PasswordTextBox fPassword;



    public void onModuleLoad() {
        LoginService.Util.getInstance().isLoggedIn(new AsyncCallback<UserData>() {
            @Override
            public void onFailure(Throwable caught) {
                String msg = caught.getMessage();
                Information.clearAll();
                if (msg == null)
                    Message.systemError("Error communicating with the server.  Possible Internet connection problem.");
                else
                    Message.systemError(msg);
            }

            @Override
            public void onSuccess(UserData result) {
//                if (result.getReturnCode() == 99) {     //  show initial GO screen
//                    RootLayoutPanel rlp = RootLayoutPanel.get();
//                    VerticalPanel vp = new VerticalPanel();
//                    final PasswordField pwf = new PasswordField();
//                    vp.add(pwf);
//
//                    final TextButton goBtn = new TextButton("Go");
//                    goBtn.addSelectHandler(new SelectHandler() {
//                        public void onSelect(SelectEvent event) {
//                            String pw = pwf.getText();
//                            if (pw != null  &&  !pw.isEmpty()  &&  pw.equals(part1()+NewUser.part2()+UserData.part3())) {
//
//                                            reloadLoginPage();      // restart the whole process but this time the user will be logged on
//                                                                    // this nuts-o, convoluted, highly fragile code is necessary to get Chrome to save a users password
//                                                                    // (I'm not talking about this single line.  The whole structure of the login procedure is dictated
//                                                                    //  by the need for this call and other crazy browser restrictions.
//                            }
//                        }
//                    });
//
//                    pwf.addKeyPressHandler(new KeyPressHandler() {
//                        @Override
//                        public void onKeyPress(KeyPressEvent event) {
//                            if (ControlUtils.isEnterKey(event.getCharCode()))
//                                goBtn.fireEvent(new SelectEvent());
//                        }
//                    });
//
//                    setInitialFocus(pwf);
//
//                    vp.add(goBtn);
//
//                    rlp.add(vp);
//                } else

                if (result.getReturnCode() == 0) {  // user already logged in
                    Information.setUser(result.getUuid(), result.getSupervisor());
                    Information.setGuest(false);

                    if (!supervisorMainLoaded)
                        Message.loadingMsg();

                    GWT.runAsync(new RunAsyncCallback() {

                        @Override
                        public void onFailure(Throwable reason) {
                            Message.loadDone();
                            Message.systemError("Code download failed.  Possible Internet connection problem.");
                        }

                        @Override
                        public void onSuccess() {
                            supervisorMainLoaded = Message.loadDone();
                            hideLoginPage();
                            new SupervisorMain().onModuleLoad();
                        }
                    });
                } else if (result.getReturnCode() == 100  ||  result.getReturnCode() == 99) {  //  display login screen
                    userLogin();
                } else {
                    Message.error(result.getMsg());
                }
            }
        });
    }

    private boolean inGuestLogin = false;

    private void hideLoginPage() {
        RootPanel.get("login-page").setVisible(false);
    }

    private void showLoginPage() {
        RootPanel.get("login-page").setVisible(true);
    }

    private void reloadLoginPage() {
        Message.deleteBackButtonMessage();
        Window.Location.reload();
    }

    private void userLogin() {

//        if (logged_in) {
//            onModuleLoad2();
//            return;
//        }
//        if (authenticated) {
//            onModuleLoad2();
//            return;
//        }
//        RootLayoutPanel rlp = RootLayoutPanel.get();
//        VerticalPanel vp = new VerticalPanel();
//        final PasswordField pwf = new PasswordField();
//        vp.add(pwf);
//
//        final TextButton goBtn = new TextButton("Go");
//        goBtn.addSelectHandler(new SelectHandler() {
//            public void onSelect(SelectEvent event) {
//                String pw = pwf.getText();
//                if (pw != null  &&  !pw.isEmpty()  &&  pw.equals(part1()+NewUser.part2()+UserData.part3())) {
//                    authenticated = true;
//                    onModuleLoad2();
//                }
//            }
//        });
//
//        pwf.addKeyPressHandler(new KeyPressHandler() {
//            @Override
//            public void onKeyPress(KeyPressEvent event) {
//                if (ControlUtils.isEnterKey(event.getCharCode()))
//                    goBtn.fireEvent(new SelectEvent());
//            }
//        });
//
//        setInitialFocus(pwf);
//
//        vp.add(goBtn);
//
//        rlp.add(vp);


        fUsername = TextBox.wrap(DOM.getElementById("username"));
        fUsername.setStyleName("gwt-TextBox");
        fPassword = PasswordTextBox.wrap(DOM.getElementById("password"));
        fPassword.setStyleName("gwt-PasswordTextBox");

        Button btnSubmit = SubmitButton.wrap(DOM.getElementById("login_submit"));

        Button.wrap(DOM.getElementById("guest-login")).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                inGuestLogin = true;
                hideLoginPage();
                guestLogin();
            }
        });


        Anchor.wrap(DOM.getElementById("more-information")).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                moreInformation();
            }
        });

        Anchor.wrap(DOM.getElementById("new-login")).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new NewUser();
            }
        });

        Anchor.wrap(DOM.getElementById("forgot-password")).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new ForgotPassword();
            }
        });

        Anchor.wrap(DOM.getElementById("why-donate")).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Message.msgOk("Although the content on BookLion was provided through the work of many people, BookLion itself was conceived, designed, built, and hosted by one person; Blake McBride.  It is owned by a one-man company named Arahant that Blake owns.  There are no partners or investors.  Any support you can offer is greatly appreciated.");
            }
        });


        FormPanel fLoginForm = FormPanel.wrap(DOM.getElementById("login_form"));  // This line must be after the submit button wrapper

        showLoginPage();  // this can only be called after all of the calls to the wrap() method

        fLoginForm.addSubmitHandler(new FormPanel.SubmitHandler() {  // ok

            @Override
            public void onSubmit(FormPanel.SubmitEvent event) {
                if (inGuestLogin)
                    return;
                String uname = fUsername.getText();
                String pw = StringUtils.encode(fPassword.getText());
                LoginService.Util.getInstance().login(uname, pw, new AsyncCallback<UserData>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        String msg = caught.getMessage();
                        Information.clearAll();
                        if (msg == null)
                            Message.systemError("Error communicating with the server.  Possible Internet connection problem.");
                        else
                            Message.systemError(msg);
                    }

                    @Override
                    public void onSuccess(UserData result) {
                        if (result.getReturnCode() != 0) {
                            fPassword.setText("");
                            Message.loadDone();
                            Information.clearAll();
                            Message.msgOk(result.getMsg());
                        } else
                            reloadLoginPage();         // restart the whole process but this time the user will be logged on
                                                       // this nuts-o, convoluted, highly fragile code is necessary to get Chrome to save a users password
                                                       // (I'm not talking about this single line.  The whole structure of the login procedure is dictated
                                                       //  by the need for this call and other crazy browser restrictions.
                    }
                });
            }
        });

    }

//	public void onModuleLoad2() {
////new Bug().onModuleLoad();
// //       test7();
////if (true)
////	return;
//
//
//        // use the following lines if mixing GWT and GXT layouts
////        RootPanel rootPanel = RootPanel.get();
////        rootPanel.clear();
////        Viewport vpt = new Viewport();
////        rootPanel.add(vpt, 0, 0);
////        LayoutPanel rlp = new LayoutPanel();
////        vpt.add(rlp);
//
//        //  else, use the following
//        RootLayoutPanel rlp = RootLayoutPanel.get();
//        rlp.clear();
//        HorizontalPanel hp;
//        Label lbl;
//        String pad = "6em";
//
//        Information.clearAll();
//        Information.setGuest(true);
//
////        CookieProvider store = new CookieProvider("/", null, "booklion.com", false);
////        store.setValue("abcde", "fghij");
////        store.getValue("abcde", new Callback<String, Throwable>() {
////            @Override
////            public void onFailure(Throwable reason) {
////
////            }
////
////            @Override
////            public void onSuccess(String result) {
////                result = result;
////            }
////        });
//
//        VerticalPanel vp = new VerticalPanel();
//
//        hp = new HorizontalPanel();
//        addSpace(hp, pad);
//        lbl = new Label("");
//        lbl.setWidth("5em");
//        hp.add(lbl);
//        Image logo = new Image("images/booklion.png");
//        logo.setSize("3in", "3in");
//        hp.add(logo);
//        vp.add(hp);
//
//
//        InlineHTML html = new InlineHTML("<center><big><big><big><big>Welcome To BookLion</big></big></big></big></center>\n");
//        vp.add(html);
//
//        addSpace(vp, "20px");
//
//
//        SimplePanel sp = new SimplePanel();
//        sp.setWidth("6in");
//        html = new InlineHTML("Keep track of all those characters.  Be reminded of the plot up to " +
//                "where you left off last week or last month. Keep notes on book you are reading." +
//                "<br><br>" +
//                "Free, user added book information that is structured and limited to where " +
//                "you are in the book - no spoilers.  Information others add that can " +
//                "help you, and information you can create, add to, and change."
//        );
//        sp.add(html);
//        vp.add(sp);
//
//
//        addSpace(vp, "10px");
//
//
//
//        ///////////////////
//
//        hp = new HorizontalPanel();
//        sp = new SimplePanel();
//        sp.setWidth("4.5in");
//        hp.add(sp);
//
//
//        vp.add(hp);
//
//        addSpace(vp, "15px");
//
//
//        //////////////////////////
//
//
//        hp = new HorizontalPanel();
//        addSpace(hp, "2em");
//        TextButton guestLoginButton = new TextButton("Continue as a guest");
//        guestLoginButton.addSelectHandler(new SelectHandler() {
//            public void onSelect(SelectEvent event) {
//                guestLogin();
//            }
//        });
//        hp.add(guestLoginButton);
//
//        addSpace(hp, "2em");
//        hp.add(new Label("or"));
//        addSpace(hp, "2em");
//
//
//        lbl = new Label("Login ID:");
//        lbl.setWidth("6em");
//        hp.add(lbl);
//
//        loginIDField = new TextField();
//        loginIDField.setName("username");
//        hp.add(loginIDField);
//        loginIDField.addKeyPressHandler(new KeyPressHandler() {
//            @Override
//            public void onKeyPress(KeyPressEvent event) {
//                if (ControlUtils.isEnterKey(event.getCharCode()))
//                    passwordField.focus();
//            }
//        });
//
//
//        addSpace(hp, "1em");
//
//        hp.add(new Label("(email address)"));
//
//        vp.add(hp);
//
//        //
//
//        addSpace(vp, "16px");
//
//        hp = new HorizontalPanel();
//        addSpace(hp, "15em");
//        lbl = new Label("Password:");
//        lbl.setWidth("6em");
//        hp.add(lbl);
//
//        passwordField = new PasswordField();
//        passwordField.setName("password");
//        hp.add(passwordField);
//
//        passwordField.addKeyPressHandler(new KeyPressHandler() {
//            @Override
//            public void onKeyPress(KeyPressEvent event) {
//                if (ControlUtils.isEnterKey(event.getCharCode()))
//                    loginButton.fireEvent(new SelectEvent());
//            }
//        });
//
//        addSpace(hp, "2em");
//
//        loginButton = new TextButton("Login");
//        loginButton.addSelectHandler(new SelectHandler() {
//            public void onSelect(SelectEvent event) {
//                attemptLogin();
//            }
//        });
//        hp.add(loginButton);
//
//
//        vp.add(hp);
//
//        addSpace(vp, "20px");
//
//
//        //
//
//        hp = new HorizontalPanel();
//
// //       addSpace(hp, "2em");
//
//        final Anchor moreInformationButton = new Anchor("More Information");
//        moreInformationButton.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                moreInformation();
//            }
//        });
//        hp.add(moreInformationButton);
//
//
//        addSpace(hp, "2em");
//
//        Anchor newLoginButton = new Anchor("Create New Login");
//        newLoginButton.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                new NewUser();
//            }
//        });
//        hp.add(newLoginButton);
//
//
////        addSpace(hp, "2em");
////
////        TextButton guestLoginButton = new TextButton("Guest Login");
////        guestLoginButton.addSelectHandler(new SelectHandler() {
////            public void onSelect(SelectEvent event) {
////                new YesNoMessageBox("Logging in as a guest will allow you to see all data, but you will not be able to change anything.  There is no cost to register or use the system.  Continue as a guest?") {
////
////                    @Override
////                    public void yes() {
////
////                    }
////
////                    @Override
////                    public void no() {
////
////                    }
////                };
////            }
////        });
////        hp.add(guestLoginButton);
//
//        addSpace(hp, "2em");
//
//        Anchor forgotPasswordButton = new Anchor("Forgot Password");
//        forgotPasswordButton.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                new ForgotPassword();
//            }
//        });
//        hp.add(forgotPasswordButton);
//
//        addSpace(hp, "3em");
//
//        html = new InlineHTML("<form action='https://www.paypal.com/cgi-bin/webscr' method='post' target='_top'> " +
//                "<input type='hidden' name='cmd' value='_s-xclick'> " +
//                "<input type='hidden' name='hosted_button_id' value='K2DF8FEJNRXCE'> " +
//                "<input type='image' src='https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif' border='0' name='submit' alt='PayPal - The safer, easier way to pay online!'> " +
//                "<img alt='' border='0' src='https://www.paypalobjects.com/en_US/i/scr/pixel.gif' width='1' height='1'> " +
//                "</form> ");
//        hp.add(html);
//
//        Anchor a = new Anchor("Why?");
//        a.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                Message.msgOk("Although the content on BookLion was provided through the work of many people, BookLion itself was conceived, designed, built, and hosted by one person; Blake McBride.  It is owned by a one-man company named Arahant that Blake owns.  There are no partners or investors.  Any support you can offer is greatly appreciated.");
//            }
//        });
//        hp.add(a);
//
//
//
//        vp.add(hp);
//
//        addSpace(vp, "10px");
//
//        html = new InlineHTML("There is no charge, or credit card needed, for any aspect of BookLion.");
//        vp.add(html);
//
//        //
//
//        rlp.add(center(vp));
//
//        InlineHTML bottomLeft = new InlineHTML("<small>Copyright \u00A9 2014 Arahant, LLC</small>");
//        rlp.add(bottomLeft);
//        rlp.setWidgetLeftWidth(bottomLeft, 0, Unit.PX, 50, Unit.PCT);
//        rlp.setWidgetBottomHeight(bottomLeft, 0, Unit.PX, 5, Unit.PCT);
//        rlp.setWidgetHorizontalPosition(bottomLeft, Alignment.BEGIN);
//        rlp.setWidgetVerticalPosition(bottomLeft, Alignment.END);
//
//        setInitialFocus(loginIDField);
//
//        rlp.getElement().getStyle().setBackgroundColor(Settings.backgroundColor);
//
//        rlp.forceLayout();
//	}

    private void moreInformation() {
        hideLoginPage();
        RootLayoutPanel rlp = RootLayoutPanel.get();
        rlp.clear();

        Message.setBackButtonMessage("If you want to go back to the BookLion login screen, stay on this page and use the Go Back button at the bottom of the screen.");

        DockLayoutPanel panel = new DockLayoutPanel(Unit.EM);

        HorizontalPanel hp = new HorizontalPanel();

        SimplePanel sp = new SimplePanel();
        sp.setSize(".5in", "1in");
        hp.add(sp);

        hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Image logo = new Image("images/smalllion.png");
        logo.setSize(".75in", ".75in");
        hp.add(logo);
        String title = "<span style=\"font-size:xx-large\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;BookLion</span>";
        hp.add(new InlineHTML(title));
        panel.addNorth(hp, 6);

        ScrollPanel scrpnl = new ScrollPanel();

        IntroText it = new IntroText();
        scrpnl.getElement().appendChild(it.getElement());

 //       panel.addEast(new Label(), 1);
        panel.addWest(new Label(), 1);

        VerticalPanel vp = new VerticalPanel();
        sp = new SimplePanel();
        sp.setSize(".5in", ".3in");
        vp.add(sp);
        TextButton okBtn = new TextButton("Go back");
        okBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                reloadLoginPage();
            }
        });
        vp.add(okBtn);

        panel.addSouth(vp, 4.5);
        panel.add(scrpnl);
        rlp.add(panel);
    }

    private static boolean supervisorMainLoaded = false;

//    private void attemptLogin() {
//		String loginID = loginIDField.getText();
//		String pw = passwordField.getText();
//
//		AsyncCallback<UserData> callback = new AsyncCallback<UserData>() {
//			public void onFailure(Throwable caught) {
//				String msg = caught.getMessage();
//                Information.clearAll();
//				if (msg == null)
//					Message.systemError("Error communicating with the server.  Possible Internet connection problem.");
//				else
//					Message.systemError(msg);
//			}
//
//			public void onSuccess(UserData result) {
//				if (result.getReturnCode() == 0) {
//                    Information.setUser(result.getUuid(), result.getSupervisor());
//                    Information.setGuest(false);
//
//                    if (!supervisorMainLoaded)
//                        Message.loadingMsg();
//
//                    GWT.runAsync(new RunAsyncCallback() {
//
//                        @Override
//                        public void onFailure(Throwable reason) {
//                            Message.loadDone();
//                            Message.systemError("Code download failed.  Possible Internet connection problem.");
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            supervisorMainLoaded = Message.loadDone();
//                            new SupervisorMain().onModuleLoad();
//                        }
//                    });
//
//
//				} else {
//                    passwordField.setText("");
//                    Message.loadDone();
//                    Information.clearAll();
//                    Message.msgOk(result.getMsg());
//				}
//			}
//
//		};
//        Message.loadingMsg("Authenticating; Please wait.");
//		loginService.login(loginID, pw, callback);
//	}

    private void guestLogin() {
        Message.msgOk("Continuing as a guest will allow you to look at any data, but you will not be able to add or change anything, and you will not be able to get support.  You will need a regular login for that.  There is no charge for a login.");

        String loginID = "_guest_";
        String pw = StringUtils.encode("ce04a5c4-efe0-4d4a-9ae7-943e986022b0");

        AsyncCallback<UserData> callback = new AsyncCallback<UserData>() {
            public void onFailure(Throwable caught) {
                String msg = caught.getMessage();
                Information.clearAll();
                if (msg == null)
                    Message.systemError("Error communicating with the server.  Possible Internet connection problem.");
                else
                    Message.systemError(msg);
            }

            public void onSuccess(UserData result) {
                if (result.getReturnCode() == 0) {
                    Information.setUser(result.getUuid(), "N");
                    Information.setGuest(true);

                    if (!supervisorMainLoaded)
                        Message.loadingMsg();

                    GWT.runAsync(new RunAsyncCallback() {

                        @Override
                        public void onFailure(Throwable reason) {
                            Message.loadDone();
                            Message.systemError("Code download failed.  Possible Internet connection problem.");
                        }

                        @Override
                        public void onSuccess() {
                            supervisorMainLoaded = Message.loadDone();
                            new SupervisorMain().onModuleLoad();
                        }
                    });


                } else {
                    passwordField.setText("");
                    Message.loadDone();
                    Information.clearAll();
                    Message.msgOk(result.getMsg());
                }
            }

        };
        Message.loadingMsg("Authenticating; Please wait.");
        loginService.login(loginID, pw, callback);
    }
	
	public static String getUUID() {
        return Information.getUserUuid();
	}
	
}
