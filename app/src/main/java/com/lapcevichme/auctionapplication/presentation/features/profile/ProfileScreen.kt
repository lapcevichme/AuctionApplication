package com.lapcevichme.auctionapplication.presentation.features.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.lapcevichme.auctionapplication.domain.model.user.UserBalance
import com.lapcevichme.auctionapplication.domain.model.user.UserMe
import com.lapcevichme.auctionapplication.domain.model.user.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Профиль", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.toggleCreateDialog(true) },
                modifier = Modifier.padding(bottom = 24.dp, end = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = state) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileUiState.Success -> {
                    ProfileContent(
                        user = currentState.user,
                        onLogoutClick = { viewModel.logout() }
                    )

                    if (currentState.showCreateDialog) {
                        CreateLotDialog(
                            title = currentState.lotTitle,
                            description = currentState.lotDescription,
                            price = currentState.lotPrice,
                            imageUri = currentState.lotImageUri,
                            onTitleChange = viewModel::updateLotTitle,
                            onDescriptionChange = viewModel::updateLotDescription,
                            onPriceChange = viewModel::updateLotPrice,
                            onImageChange = viewModel::updateLotImage,
                            onDismiss = { viewModel.toggleCreateDialog(false) },
                            onConfirm = viewModel::createLot
                        )
                    }
                }
                is ProfileUiState.Error -> {
                    ErrorMessage(currentState.message, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: UserMe,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier
                .size(110.dp)
                .padding(4.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(28.dp))

        BalanceCard(
            available = user.balance.available,
            frozen = user.balance.frozen,
            scale = user.balance.scale
        )

        Spacer(modifier = Modifier.height(24.dp))

        InfoSection(
            icon = Icons.Default.Info,
            label = "Роль в системе",
            value = if (user.role == UserRole.SELLER) "Продавец" else "Покупатель",
            valueColor = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        TextButton(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Выйти из аккаунта")
        }
    }
}

@Composable
private fun BalanceCard(available: Long, frozen: Long, scale: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Общий баланс",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = formatBalance(available, scale),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Заморожено:", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = formatBalance(frozen, scale),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateLotDialog(
    title: String,
    description: String,
    price: String,
    imageUri: Uri?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onImageChange: (Uri?) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> onImageChange(uri) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = title.isNotBlank() && price.isNotBlank()
            ) {
                Text("Создать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
        title = { Text("Новый лот") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                            Text("Выбрать фото", style = MaterialTheme.typography.labelMedium)
                        }
                    } else {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = onPriceChange,
                    label = { Text("Начальная цена") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }
    )
}

@Composable
private fun InfoSection(icon: ImageVector, label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleMedium, color = valueColor, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ErrorMessage(message: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}

private fun formatBalance(amount: Long, scale: Int): String {
    return "$amount ₽"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    state: ProfileUiState,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onToggleDialog: (Boolean) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onImageChange: (Uri?) -> Unit,
    onConfirmCreate: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Профиль", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onToggleDialog(true) },
                modifier = Modifier.padding(bottom = 24.dp, end = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileUiState.Success -> {
                    ProfileContent(
                        user = state.user,
                        onLogoutClick = onLogoutClick
                    )

                    if (state.showCreateDialog) {
                        CreateLotDialog(
                            title = state.lotTitle,
                            description = state.lotDescription,
                            price = state.lotPrice,
                            imageUri = state.lotImageUri,
                            onTitleChange = onTitleChange,
                            onDescriptionChange = onDescriptionChange,
                            onPriceChange = onPriceChange,
                            onImageChange = onImageChange,
                            onDismiss = { onToggleDialog(false) },
                            onConfirm = onConfirmCreate
                        )
                    }
                }
                is ProfileUiState.Error -> {
                    ErrorMessage(state.message, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Profile Screen - Success")
@Composable
fun ProfileScreenPreview_Success() {
    val mockUser = UserMe(
        email = "example@mail.com",
        name = "Иван Иванов",
        role = UserRole.SELLER,
        avatarUrl = null,
        balance = UserBalance(available = 150000, frozen = 5000, scale = 2)
    )

    MaterialTheme {
        ProfileScreenContent(
            state = ProfileUiState.Success(user = mockUser),
            onBackClick = {},
            onLogoutClick = {},
            onToggleDialog = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPriceChange = {},
            onImageChange = {},
            onConfirmCreate = {}
        )
    }
}

@Preview(showBackground = true, name = "Create Lot Dialog")
@Composable
fun CreateLotDialogPreview() {
    MaterialTheme {
        CreateLotDialog(
            title = "Название антикварного лота",
            description = "Краткое описание характеристик лота",
            price = "5000",
            imageUri = null,
            onTitleChange = {},
            onDescriptionChange = {},
            onPriceChange = {},
            onImageChange = {},
            onDismiss = {},
            onConfirm = {}
        )
    }
}

@Preview(showBackground = true, name = "Profile Screen - Loading")
@Composable
fun ProfileScreenPreview_Loading() {
    MaterialTheme {
        ProfileScreenContent(
            state = ProfileUiState.Loading,
            onBackClick = {},
            onLogoutClick = {},
            onToggleDialog = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPriceChange = {},
            onImageChange = {},
            onConfirmCreate = {}
        )
    }
}