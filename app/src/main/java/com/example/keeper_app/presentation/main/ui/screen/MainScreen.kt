package com.example.keeper_app.presentation.main.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keeper_app.R
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.data.storage.entities.Totp
import com.example.keeper_app.presentation.main.ui.components.DeleteServiceDialog
import com.example.keeper_app.presentation.main.ui.components.DetailServiceDialog
import com.example.keeper_app.presentation.main.ui.components.RenameServiceDialog
import com.example.keeper_app.presentation.main.viewmodel.MainState
import com.example.keeper_app.presentation.main.viewmodel.MainViewModel
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.LocalColors
import com.example.keeper_app.presentation.ui.theme.Red
import com.example.keeper_app.presentation.ui.theme.custom.CustomStatusBar
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    onNavigateToAddService: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
){

    val viewModel: MainViewModel = hiltViewModel()
    val uiState by viewModel.mainState.collectAsState()

    MainContent(
        uiState = uiState,
        onSettingsClick = onSettingsClick,
        onAboutClick = onAboutClick,
        onNavigateToAddService = onNavigateToAddService,
    )
}


@Composable
private fun MainContent(
    uiState: MainState,
    onNavigateToAddService: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
){
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDetailServiceDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<ServiceDb?>(null) }

    if (showDetailServiceDialog && selectedService != null){
        DetailServiceDialog(
            service = selectedService,
            onDismiss = {
                showDetailServiceDialog = false
                selectedService = null
            }
        )
    }

    if(showRenameDialog && selectedService != null){
        RenameServiceDialog(
            service = selectedService,
            onConfirm = {
                //функция переименовки
                showRenameDialog = false
                selectedService = null
            },
            onDismiss = {
                showRenameDialog = false
                selectedService = null
            }
        )
    }

    if(showDeleteDialog && selectedService != null){
        DeleteServiceDialog(
            service = selectedService,
            onConfirm = {
                //функция удаления
                showRenameDialog = false
                selectedService = null
            },
            onDismiss = {
                showRenameDialog = false
                selectedService = null
            }
        )
    }
    CustomStatusBar()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (
                modifier = Modifier.width(280.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                windowInsets = WindowInsets(0.dp),
                drawerShape = RectangleShape
            ){
                DrawerHeader()
                DrawerItem(
                    label = stringResource(R.string.drawer_settings),
                    onClick = onSettingsClick
                )
                DrawerItem(
                    label = stringResource(R.string.drawer_about),
                    onClick = onAboutClick
                )

            }
        },
        scrimColor = MaterialTheme.colorScheme.scrim,
        modifier = Modifier
            .padding(
            top = WindowInsets.statusBars.asPaddingValues()
                .calculateTopPadding()
        ),
        content = {
            Scaffold (
                modifier = Modifier.fillMaxSize(),

                topBar = {
                    Column {
                        CustomTopBar(
                            onMenuClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.LocalColors.appBar.shape))
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier.size(width = 50.dp, height = 50.dp),
                        onClick = onNavigateToAddService,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null,
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large_super))
                        )
                    }
                },
                floatingActionButtonPosition = FabPosition.End
            ){ paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                ) {
                    HomeContent(
                        uiState = uiState,
                        onDetailClick = { service ->
                            selectedService = service
                            showDetailServiceDialog = true
                        },
                        onMoreClick = { service ->
                            selectedService = service
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun HomeContent(
    uiState: MainState,
    onDetailClick: (ServiceDb) -> Unit,
    onMoreClick: (ServiceDb) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_body))
    ){
        when(uiState){
            is MainState.Idle -> {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement =  Arrangement.Center
                ){

                    Text(
                        text = stringResource(R.string.service_start_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

            }
            is MainState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            is MainState.Success -> {
                val service = uiState.service

                if(service.isEmpty()){
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement =  Arrangement.Center
                    ){

                        Column (
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement =  Arrangement.Center
                        ){

                            Text(
                                text = stringResource(R.string.service_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }else{
                    LazyColumn {
                        items (service) { service ->
                            var expanded by remember { mutableStateOf(false) }

                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = service.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                trailingContent = {
                                    Box {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_more_vert),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(dimensionResource(R.dimen.icon_size_medium))
                                                .clickable{expanded = !expanded},
                                        )

                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            shape = MaterialTheme.shapes.medium,
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            tonalElevation = 0.dp,
                                        ) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text("Переименовать")
                                                },
                                                onClick = {
                                                    expanded = false
                                                    onMoreClick(service)
                                                },
                                                modifier = Modifier.height(44.dp),
                                                contentPadding = PaddingValues(vertical = 0.dp, horizontal = dimensionResource(R.dimen.padding_more_menu))
                                            )
                                            Spacer(modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(MaterialTheme.colorScheme.outline)
                                            )
                                            DropdownMenuItem(
                                                text = {
                                                    Text("Удалить")
                                                },
                                                onClick = {
                                                    expanded = false
                                                    onMoreClick(service)
                                                },
                                                modifier = Modifier.height(44.dp),
                                                contentPadding = PaddingValues(vertical = 0.dp, horizontal = dimensionResource(R.dimen.padding_more_menu))
                                            )
                                        }
                                    }

                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        shape = MaterialTheme.shapes.medium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    .clickable { onDetailClick }
                                    .heightIn(min = 50.dp)
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium)))
                        }
                    }
                }
            }
            is MainState.Error -> {
                Text(
                    text = stringResource(R.string.service_error_load),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTopBar(
    onMenuClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Icon(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(100.dp, 50.dp),
                tint = null
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.appBar_descMenu)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.LocalColors.appBar.containerColor,
            titleContentColor = MaterialTheme.LocalColors.appBar.titleContentColor,
            navigationIconContentColor = MaterialTheme.LocalColors.appBar.navigationIconContentColor,
            actionIconContentColor = MaterialTheme.LocalColors.appBar.actionIconContentColor
        ),
        expandedHeight = 70.dp,
        windowInsets =  WindowInsets(0.dp)
    )

}

@Composable
private fun DrawerHeader() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.LocalColors.drawer.background)
            .padding(vertical = 24.dp, horizontal = 16.dp)

    ) {
        Text(
            text ="Меню",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.LocalColors.drawer.text
        )
    }
}

@Composable
private fun DrawerItem(
    label: String,
    selected: Boolean = false,
    onClick: () -> Unit
){
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.height(53.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.LocalColors.drawer.selectedContainerColor,
            unselectedContainerColor = MaterialTheme.LocalColors.drawer.unselectedContainerColor,
            selectedIconColor = MaterialTheme.LocalColors.drawer.selectedIconColor,
            unselectedIconColor = MaterialTheme.LocalColors.drawer.unselectedIconColor,
            selectedTextColor = MaterialTheme.LocalColors.drawer.selectedTextColor,
            unselectedTextColor = MaterialTheme.LocalColors.drawer.unselectedTextColor,
            selectedBadgeColor = MaterialTheme.LocalColors.drawer.selectedBadgeColor,
            unselectedBadgeColor = MaterialTheme.LocalColors.drawer.unselectedBadgeColor,
        )
    )
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(MaterialTheme.LocalColors.drawer.shape))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainContentPreview(){
    val previewServices = listOf(
        ServiceDb(
            id = 1,
            userId = "user123",
            name = "Google",
            totp = Totp(secretKey = "JBSWY3DPEHPK3PXP")
        ),
        ServiceDb(
            id = 2,
            userId = "user123",
            name = "GitHub",
            totp = Totp(secretKey = "KRTGC3DPEHPK3PXP")
        ),
        ServiceDb(
            id = 3,
            userId = "user123",
            name = "Microsoft",
            totp = Totp(secretKey = "LRTGC4DPEHPK3PXP")
        ),
        ServiceDb(
            id = 4,
            userId = "user123",
            name = "Apple",
            totp = Totp(secretKey = "JBSWY3DPEHP23233")
        ),
    )

    // Создаём нужное состояние
    val previewUiState = MainState.Success(previewServices)

    AppTheme {
        MainContent(
            uiState = previewUiState,
            onSettingsClick = {},
            onAboutClick = {},
            onNavigateToAddService = {},
        )
    }
}

