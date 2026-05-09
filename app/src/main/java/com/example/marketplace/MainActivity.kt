package com.example.marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.Format
import java.text.NumberFormat
import java.util.Locale

data class Product(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val price: Int,
    val description: String,
    val imageRes: Int = R.drawable.item_icon_craftitem_0
)

val KivotosBlue = Color(0xFF4DA8FF)
val KivotosSky = Color(0xFFBDE3FF)
val KivotosBackground = Color(0xFFF5FAFF)
val KivotosCard = Color(0xFFFFFFFF)
val KivotosAccent = Color(0xFFFFD6E7)
val KivotosText = Color(0xFF1F2A44)
val creditFormatter: Format = NumberFormat.getNumberInstance(Locale.US)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarketplaceTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MarketplaceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = KivotosBlue,
            secondary = KivotosAccent,
            background = KivotosBackground,
            surface = KivotosCard,
            onPrimary = Color.White,
            onBackground = KivotosText
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("home") }
    val productList = remember {
        mutableStateListOf(
            Product(
                name = "Energy Drink (S)",
                price = 15000,
                description = "An energy drink for adults that instantly blasts away" +
                        " the slightest hint of fatigue. Its ingredients are a mystery," +
                        " but it definitely works.",
                imageRes = R.drawable.item_icon_consumable_ap_0
            ),
            Product(
                name = "Macaron",
                price = 10000,
                description = "A palm-sized macaron. Various colors and flavors are available.",
                imageRes = R.drawable.item_icon_event_token_2_s18
            ),
            Product(
                name = "Ice Cooler",
                price = 95000,
                description = "An ice cooler that can store fruit and drinks" +
                        " at a cool temperature. It's also effective at" +
                        " storing temperature-sensitive firearms.",
                imageRes = R.drawable.item_icon_event_object_00_s6
            ),
            Product(
                name = "Keycap Toy",
                price = 5000,
                description = "A fidget toy made with a keycap from a keyboard." +
                        " Nothing actually happens when it's pressed," +
                        " but the satisfying clicking sound can calm down an anxious mind.",
                imageRes = R.drawable.equipment_icon_charm_tier10
            ),
            Product(
                name = "Waterproof Digital Watch",
                price = 225000,
                description = "A digital watch produced by the sportswear brand Serval." +
                        " Because it's waterproof, there is no need to fear ruining it," +
                        " even if you wear it while swimming.",
                imageRes = R.drawable.equipment_icon_watch_tier1
            )
        )
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            when (currentScreen) {
                "add" -> {
                    AddItemTopBar(onBackClick = { currentScreen = "home" })
                }
                else -> {
                    HomeTopBar()
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 10.dp,
                modifier = Modifier.clip(
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
            ) {
                NavigationBarItem(
                    selected = currentScreen == "home",
                    onClick = { currentScreen = "home" },
                    label = { Text("Home") },
                    icon = { Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home"
                    ) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KivotosBlue,
                        selectedTextColor = KivotosBlue,
                        indicatorColor = KivotosSky,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == "profile",
                    onClick = { currentScreen = "profile" },
                    label = { Text("Profile") },
                    icon = { Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Quest"
                    ) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KivotosBlue,
                        selectedTextColor = KivotosBlue,
                        indicatorColor = KivotosSky,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        },
        floatingActionButton = {
            if (currentScreen == "home") {
                ExtendedFloatingActionButton(
                    onClick = { currentScreen = "add" },
                    shape = RoundedCornerShape(20.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                    containerColor = KivotosBlue,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Sell Items", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF7FBFF), Color(0xFFEAF4FF))
                    )
                )
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "Screen Animation"
            ) { screen ->
                when (screen) {
                    "home" -> HomeScreen(productList)
                    "add" -> AddProductScreen(
                        onProductAdded = { newProduct ->
                            productList.add(0, newProduct)
                            scope.launch {
                                currentScreen = "home"
                                snackbarHostState.showSnackbar(
                                    message = "Product successfully added!"
                                )
                            }
                        }
                    )
                    "profile" -> ProfileScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        modifier = Modifier.padding(bottom = 8.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Column(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.angel24_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Angel24 ",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 21.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Marketplace",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 21.sp,
                            color = KivotosBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(text = "Student Marketplace", fontSize = 12.sp, color = Color.Gray)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemTopBar(
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.padding(bottom = 8.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Text(
                text = "Add Item",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
    )
}

@Composable
fun HomeScreen(products: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = KivotosSky.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Hello, Hayase Yuuka!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Find what you need today.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                    )
                }
            }
        }
        items(products) { product ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(shape = RoundedCornerShape(18.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = KivotosSky.copy(alpha = 0.7f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = R.drawable.currency_icon_gold
                                        ),
                                        contentDescription = "Credits",
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = creditFormatter.format(product.price),
                                        color = KivotosBlue,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = product.description,
                                color = Color.DarkGray,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    HorizontalDivider(color = KivotosSky.copy(alpha = 0.5f))

                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = KivotosBlue)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "Details", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddProductScreen(onProductAdded: (Product) -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Sell New Items",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "Create a new marketplace listing.", color = Color.Gray)
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = KivotosSky.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.item_icon_craftitem_0),
                            contentDescription = "Preview",
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Preview of your marketplace listing.",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = KivotosBlue),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_a_photo_24px),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "Add Image", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                text = "Item Information",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = KivotosText
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.box_24px),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KivotosBlue,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLeadingIconColor = KivotosBlue,
                            unfocusedLeadingIconColor = Color.LightGray
                        )
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { if (it.all { char -> char.isDigit() }) { price = it } },
                        label = { Text("Price") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.currency_icon_gold),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KivotosBlue,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLeadingIconColor = KivotosBlue,
                            unfocusedLeadingIconColor = Color.LightGray
                        )
                    )

                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.text_ad_24px),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KivotosBlue,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLeadingIconColor = KivotosBlue,
                            unfocusedLeadingIconColor = Color.LightGray
                        )
                    )

                    HorizontalDivider(color = KivotosSky.copy(alpha = 0.5f))

                    Button(
                        onClick = {
                            isLoading = true
                            scope.launch {
                                delay(1000)
                                onProductAdded(
                                    Product(
                                        name = name,
                                        price = price.toInt(),
                                        description = desc
                                    )
                                )
                                isLoading = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        enabled = name.isNotBlank() &&
                                price.isNotBlank() &&
                                !isLoading,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KivotosBlue)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(text = "Save Product", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ProfileScreen() {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Hayase Yuuka",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Seminar Treasurer",
                        color = KivotosBlue,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Millennium Science School", color = Color.Gray)

                    Spacer(modifier = Modifier.height(20.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StudentStat(
                            icon = R.drawable.box_24px,
                            title = "Items",
                            value = "12"
                        )
                        StudentStat(
                            icon = R.drawable.chart_data_24px,
                            title = "Sales",
                            value = "48"
                        )
                        StudentStat(
                            icon = R.drawable.star_24px,
                            title = "Rating",
                            value = "4.9"
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "System",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = KivotosText
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Default.Edit,
                        title = "Edit Profile"
                    )

                    HorizontalDivider(color = KivotosSky.copy(alpha = 0.5f))

                    ProfileMenuItem(
                        icon = Icons.Default.ShoppingCart,
                        title = "Your Items"
                    )

                    HorizontalDivider(color = KivotosSky.copy(alpha = 0.5f))

                    ProfileMenuItem(
                        icon = Icons.Default.Settings,
                        title = "Settings"
                    )

                    HorizontalDivider(color = KivotosSky.copy(alpha = 0.5f))

                    ProfileMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications"
                    )

                    HorizontalDivider(color = KivotosSky.copy(alpha = 0.5f))

                    ProfileMenuItem(
                        icon = Icons.Default.Person,
                        title = "Help Center"
                    )

                    HorizontalDivider(color = KivotosSky.copy(alpha = 0.5f))

                    ProfileMenuItem(
                        icon = Icons.Default.Info,
                        title = "About"
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = KivotosBlue
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun StudentStat(icon: Int, title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(KivotosSky),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = KivotosBlue,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = KivotosBlue
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}
