package com.lapcevichme.auctionapplication.presentation.features.lots.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lapcevichme.auctionapplication.domain.model.bid.Bid
import com.lapcevichme.auctionapplication.domain.model.lot.Lot
import com.lapcevichme.auctionapplication.domain.model.lot.LotStatus
import com.lapcevichme.auctionapplication.domain.model.user.User
import com.lapcevichme.auctionapplication.domain.model.user.UserRole
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotDetailsScreen(
    state: LotDetailsUiState,
    onBackClick: () -> Unit,
    onPlaceBidClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Детали лота") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (state is LotDetailsUiState.Success && state.lot?.status == LotStatus.ACTIVE) {
                Surface(
                    tonalElevation = 3.dp,
                    shadowElevation = 8.dp
                ) {
                    Button(
                        onClick = onPlaceBidClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Сделать ставку")
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is LotDetailsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LotDetailsUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                is LotDetailsUiState.Success -> {
                    state.lot?.let { lot ->
                        LotDetailsContent(lot = lot, bids = state.bids)
                    }
                }
            }
        }
    }
}

@Composable
private fun LotDetailsContent(lot: Lot, bids: List<Bid>) {
    val scrollState = rememberScrollState()
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = lot.pictureUrl,
                contentDescription = lot.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            val (statusBg, statusText) = if (lot.status == LotStatus.ACTIVE) {
                MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
            }

            Surface(
                modifier = Modifier.padding(16.dp).align(Alignment.TopEnd),
                color = statusBg.copy(alpha = 0.9f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (lot.status == LotStatus.ACTIVE) "АКТИВЕН" else "ЗАКРЫТ",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = statusText
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = lot.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                lot.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Текущая цена",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${lot.currentPrice} ₽",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(
                                    text = "Начальная",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${lot.originalPrice} ₽",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Шаг ставки",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "от ${lot.minBidStep} ₽",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Участники",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(lot.creator.name.take(1).uppercase(), style = MaterialTheme.typography.titleMedium)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Продавец", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(lot.creator.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "История ставок",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        if (bids.isEmpty()) {
                            Text(
                                text = "Ставок пока нет. Будьте первым!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                bids.forEach { bid ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = bid.bidder.name.take(1).uppercase(),
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = bid.bidder.name,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                            )
                                            Text(
                                                text = bid.time.format(timeFormatter),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Text(
                                            text = "${bid.amount} ₽",
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                            color = if (bid.id == lot.highestBid?.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Сроки",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Создан", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = lot.createdAt.toLocalDate().toString(), style = MaterialTheme.typography.bodyMedium)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Завершение", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = lot.expirationDate.format(dateFormatter),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

private class LotDetailsUiStateProvider : PreviewParameterProvider<LotDetailsUiState> {
    val user = User(name = "Виктор П.", role = UserRole.SELLER, avatarUrl = null, id = "123")
    val bidder1 = User(name ="Мария К.", role = UserRole.BUYER, avatarUrl = null, id = "123")
    val bidder2 = User(name ="Иван И.", role = UserRole.BUYER, avatarUrl = null, id = "123")

    val mockBids = listOf(
        Bid("b3", bidder1, 28000, OffsetDateTime.now().minusMinutes(5)),
        Bid("b2", bidder2, 27500, OffsetDateTime.now().minusHours(1)),
        Bid("b1", bidder1, 26000, OffsetDateTime.now().minusHours(4))
    )

    val lot = Lot(
        id = "1",
        title = "Антикварные карманные часы",
        description = "Механические часы конца XIX века. Исправны, недавно прошли чистку у мастера. Корпус из серебра 800 пробы.",
        pictureUrl = null,
        creator = user,
        currentPrice = 28000,
        originalPrice = 20000,
        minBidStep = 500,
        highestBid = mockBids.first(),
        createdAt = OffsetDateTime.now().minusDays(3),
        expirationDate = OffsetDateTime.now().plusDays(2),
        status = LotStatus.ACTIVE
    )

    override val values = sequenceOf(
        LotDetailsUiState.Loading,
        LotDetailsUiState.Success(lot, mockBids),
        LotDetailsUiState.Error("Ошибка загрузки данных")
    )
}

@Preview(showBackground = true, heightDp = 1400)
@Composable
private fun LotDetailsScreenPreview(
    @PreviewParameter(LotDetailsUiStateProvider::class) state: LotDetailsUiState
) {
    MaterialTheme {
        LotDetailsScreen(state = state, onBackClick = {}, onPlaceBidClick = {})
    }
}