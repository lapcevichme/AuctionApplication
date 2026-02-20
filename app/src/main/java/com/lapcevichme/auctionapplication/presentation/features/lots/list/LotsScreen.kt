package com.lapcevichme.auctionapplication.presentation.features.lots.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lapcevichme.auctionapplication.domain.model.bid.Bid
import com.lapcevichme.auctionapplication.domain.model.lot.LotStatus
import com.lapcevichme.auctionapplication.domain.model.lot.LotSummary
import com.lapcevichme.auctionapplication.domain.model.user.User
import com.lapcevichme.auctionapplication.domain.model.user.UserRole
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotsScreen(
    state: LotsUiState,
    onQueryChange: (String) -> Unit = {},
    onSearchActiveChange: (Boolean) -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Лоты") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val isSearchActive = (state as? LotsUiState.Success)?.isSearchActive ?: false
            val searchQuery = (state as? LotsUiState.Success)?.searchQuery ?: ""

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isSearchActive) 0.dp else 16.dp)
                    .padding(bottom = 8.dp)
            ) {
                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    query = searchQuery,
                    onQueryChange = onQueryChange,
                    onSearch = { },
                    active = isSearchActive,
                    onActiveChange = onSearchActiveChange,
                    placeholder = { Text("Поиск лотов") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    },
                    enabled = state !is LotsUiState.Loading,
                    shape = if (isSearchActive) RoundedCornerShape(0.dp) else RoundedCornerShape(12.dp),
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    if (state is LotsUiState.Success) {
                        LotsContent(lots = state.lots)
                    }
                }
            }

            when (state) {
                is LotsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is LotsUiState.Success -> {
                    if (!state.isSearchActive) {
                        LotsContent(lots = state.lots)
                    }
                }
                is LotsUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun LotsContent(
    lots: List<LotSummary>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(lots) { lot ->
            LotCard(
                lot = lot,
                onClick = { }
            )
        }
    }
}

@Composable
fun LotCard(
    lot: LotSummary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lot.creator.name.take(1).uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Column {
                    Text(
                        text = lot.creator.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "ПРОДАВЕЦ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(20.dp))
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
                    MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                }

                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    color = statusBg.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (lot.status == LotStatus.ACTIVE) "АКТИВЕН" else "ЗАВЕРШЕН",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = statusText
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = lot.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "ЦЕНА",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${lot.currentPrice} ₽",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (lot.highestBid != null) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "ЛИДЕР",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = lot.highestBid.bidder.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Завершится ${lot.expirationDate.format(DateTimeFormatter.ofPattern("d MMMM"))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private class LotsUiStateProvider : PreviewParameterProvider<LotsUiState> {
    val mockUser1 = User(id = "0", name = "Александр Пушкин", role = UserRole.SELLER, avatarUrl = null)
    val mockUser2 = User(id = "0", name = "Иван Иванов", role = UserRole.BUYER, avatarUrl = null)

    val mockLots = listOf(
        LotSummary(
            id = "1",
            title = "Винтажная камера Zenit-E",
            description = "В отличном состоянии",
            pictureUrl = null,
            creator = mockUser1,
            currentPrice = 15000,
            highestBid = Bid("b1", mockUser2, 16500, OffsetDateTime.now()),
            createdAt = OffsetDateTime.now(),
            expirationDate = OffsetDateTime.now().plusDays(5),
            status = LotStatus.CLOSED
        ),
        LotSummary(
            id = "2",
            title = "Набор виниловых пластинок",
            description = "Классика рока",
            pictureUrl = null,
            creator = mockUser1,
            currentPrice = 8000,
            highestBid = null,
            createdAt = OffsetDateTime.now(),
            expirationDate = OffsetDateTime.now().plusHours(12),
            status = LotStatus.ACTIVE
        )
    )

    override val values = sequenceOf(
        LotsUiState.Loading,
        LotsUiState.Success(lots = mockLots),
        LotsUiState.Success(lots = emptyList()),
        LotsUiState.Error(message = "Ошибка при загрузке данных")
    )
}

@Preview(showBackground = true, name = "Lots Screen States")
@Composable
fun LotsScreenPreview(
    @PreviewParameter(LotsUiStateProvider::class) state: LotsUiState
) {
    MaterialTheme {
        LotsScreen(state = state)
    }
}

@Preview(showBackground = true, name = "Lot Card Active")
@Composable
fun LotCardPreview() {
    val mockUser = User(id = "0", name = "Константин", role = UserRole.SELLER, avatarUrl = null)
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LotCard(
                lot = LotSummary(
                    id = "123",
                    title = "Коллекционные часы",
                    description = "Описание лота",
                    pictureUrl = null,
                    creator = mockUser,
                    currentPrice = 45000,
                    highestBid = Bid("1", mockUser, 47000, OffsetDateTime.now()),
                    createdAt = OffsetDateTime.now(),
                    expirationDate = OffsetDateTime.now().plusDays(2),
                    status = LotStatus.ACTIVE
                )
            )
        }
    }
}