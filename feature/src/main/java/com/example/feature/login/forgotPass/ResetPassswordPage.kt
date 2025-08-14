package com.example.feature.login.forgotPass

import androidx.compose.runtime.Composable

@Composable
fun ResetPasswordPage(

) {

}

//@Composable
//fun ResetPasswordScreen(
//    supabase: SupabaseClient,
//    navController: NavController,
//    deepLinkUri: Uri?
//) {
//    var newPassword by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    val scope = rememberCoroutineScope()
//
//    val accessToken = deepLinkUri?.getQueryParameter("access_token")
//    val type = deepLinkUri?.getQueryParameter("type")
//
//    LaunchedEffect(accessToken) {
//        if (!accessToken.isNullOrEmpty() && type == "recovery") {
//            try {
//                supabase.auth.exchangeCodeForSession(accessToken)
//                println("Session restored for password reset")
//            } catch (e: Exception) {
//                println("Failed to restore session: ${e.message}")
//            }
//        }
//    }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        OutlinedTextField(
//            value = newPassword,
//            onValueChange = { newPassword = it },
//            label = { Text("New Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth()
//        )
//        OutlinedTextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            label = { Text("Confirm Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                if (newPassword == confirmPassword && newPassword.length >= 6) {
//                    scope.launch {
//                        try {
//                            supabase.auth.updateUser {
//                                password = newPassword
//                            }
//                            navController.popBackStack()
//                            println("Password updated successfully")
//                        } catch (e: Exception) {
//                            println("Password update failed: ${e.message}")
//                        }
//                    }
//                } else {
//                    println("Passwords do not match or are too short")
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Update Password")
//        }
//    }
//}
//
////            val navController = rememberNavController()
////            NavGraph(navController = navController, activity = this)
//
////            NavHost(navController, startDestination = "home") {
////                composable("home") {
////                    HomeScreen(supabase)
////                }
////                composable(
////                    route = "reset",
////                    deepLinks = listOf(
////                        navDeepLink {
////                            uriPattern = "beanyapp://password-reset"
////                        }
////                    )
////                ) { backStackEntry ->
////                    val uri = backStackEntry.arguments?.getString("android-support-nav:controller:deepLinkIntent")
////                    val deepLinkUri = intent?.data
////                    ResetPasswordScreen(supabase, navController, deepLinkUri)
////                }
////            }
