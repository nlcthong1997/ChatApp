# ChatApp

### Intro
```sh
- The app chat use Socket java.
- The application is written for required my learning requirements, and for those of 
  you who need a reference to basic sockets.
- Allows users to send messages and send files to other active users at the same time.
```


### What I use in my application and how to organize my apps
#### 1. Java libraries
| # | Some of the main libraries |
|---|---|
| 1 | java.net.ServerSocket |
| 2 | java.net.Socket |
| 3 | java.io.ObjectInputStream |
| 4 | java.io.ObjectOutputStream |
| 5 | java.io.Serializable |
| 6 | java.swing.* |

#### 2. Organize
```sh
ChatApp
   |-chatapp.actionEnum
   |    |-ActionEnum.java
   |-chatapp.common
   |    |-FileCommon.java
   |    |-Server.java
   |-chatapp.controllers
   |    |-LoginController.java
   |    |-RegisterController.java
   |-chatapp.model
   |    |-Content.java
   |    |-FileInfo.java
   |    |-User.java
   |-chatapp.view
        |-ChatView.java
        |-LoginView.java
        |-RegisterView.java
```

### Describes the flow of action and feature
#### 1. Server
  - Receive and direct data to the target user, with each user being a thread
  - Contains a list of active users
  - Distinguish post requests from users such as Update, Exit, Send messages/files with key-value requests
#### 2. Client
  - Login and registration, user information is saved in the file as an objects info.
  - The current user's message with other users is saved here and when the user exits the chat is deleted
  - View the list of active users
  - Send message, files
  - See the list of received files
  - Save file









