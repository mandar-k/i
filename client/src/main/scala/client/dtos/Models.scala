package client.dtos

/**
  * Created by bhagyashree.b on 24-12-2016.
  */

case class UserModel(name: String = "",
                     email: String = "",
                     password: String = "",
                     isLoggedIn: Boolean = false,
                     imgSrc: String = "",
                     confirmPassword: String = "",
                     isAvailable:Boolean=true,
                     balanceAmp: String = "0.00",
                     balanceBtc: String = "0.00",
                     address: String = "n/a",
                     networkMode: String = "TestNet")

case class SignUpModel(email: String = "",
                       password: String = "",
                       confirmPassword: String = "",
                       name: String = "",
                       lastName: String = "",
                       createBTCWallet: Boolean = false,
                       isModerator: Boolean = false,
                       isClient: Boolean = false,
                       isFreelancer: Boolean = false,
                       canReceiveEmailUpdates: Boolean = false,
                       isLoggedIn: Boolean = false,
                       imgSrc: String = "",
                       didAcceptTerms: Boolean = false)