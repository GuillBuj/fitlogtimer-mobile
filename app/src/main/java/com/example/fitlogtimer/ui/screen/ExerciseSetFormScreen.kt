package com.example.fitlogtimer.ui.screen

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitlogtimer.data.local.JsonDataManager
import com.example.fitlogtimer.data.remote.drive.GoogleDriveManager
import com.example.fitlogtimer.data.repository.DataRepository
import com.example.fitlogtimer.data.repository.DriveRepository
import com.example.fitlogtimer.ui.component.AppTopBar
import com.example.fitlogtimer.ui.component.BandsInput
import com.example.fitlogtimer.ui.component.BodyWeightInputIcon
import com.example.fitlogtimer.ui.component.Chrono
import com.example.fitlogtimer.ui.component.DistanceInput
import com.example.fitlogtimer.ui.component.DurationInput
import com.example.fitlogtimer.ui.component.ExerciseDropdown
import com.example.fitlogtimer.ui.component.ExportJsonButton
import com.example.fitlogtimer.ui.component.RepsInput
import com.example.fitlogtimer.ui.component.Timer
import com.example.fitlogtimer.ui.component.WeightInput
import com.example.fitlogtimer.ui.component.WorkoutTypeInputIcon
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModelFactory

@SuppressLint("DefaultLocale")
@Composable
fun ExerciseSetFormScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val jsonDataManager = remember { JsonDataManager(context) }
    val driveManager = remember { GoogleDriveManager(context) }

    val repository = remember { DataRepository(jsonDataManager, driveManager) }
    val driveRepository = remember { DriveRepository(context) }

    val viewModel: ExerciseSetViewModel = viewModel(
        factory = ExerciseSetViewModelFactory(
            application = application,
            repository = repository,
            driveRepository = driveRepository,
            jsonDataManager = jsonDataManager
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedExercise = uiState.exercises.find { it.id == uiState.selectedExerciseId }

    Scaffold(
        topBar = { AppTopBar(modifier = Modifier.statusBarsPadding()) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Ajouter des séries",
                    style = MaterialTheme.typography.headlineMedium
                )

                WorkoutTypeInputIcon(viewModel = viewModel)
                BodyWeightInputIcon(viewModel = viewModel)
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Chargement des exercices...")
                    }
                }

                uiState.exercises.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Aucun exercice trouvé dans le fichier JSON")
                    }
                }

                else -> {
                    // Sélecteur d’exercice
                    ExerciseDropdown(
                        exercises = uiState.exercises,
                        selectedExerciseId = uiState.selectedExerciseId,
                        onExerciseSelected = viewModel::updateSelectedExercise
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    when (selectedExercise?.type) {
                        "FREE_WEIGHT" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                RepsInput(
                                    reps = uiState.reps,
                                    onRepsChange = viewModel::updateReps,
                                    modifier = Modifier.weight(1f)
                                )
                                WeightInput(
                                    weight = uiState.weight,
                                    onWeightChange = viewModel::updateWeight,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        "ELASTIC" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                RepsInput(
                                    reps = uiState.reps,
                                    onRepsChange = viewModel::updateReps,
                                    modifier = Modifier.weight(1f)
                                )
                                BandsInput(
                                    bands = uiState.bands,
                                    onBandsChange = viewModel::updateBands,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        "ISOMETRIC" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                DurationInput(
                                    duration = uiState.durationS,
                                    onDurationChange = viewModel::updateDuration,
                                    modifier = Modifier.weight(1f)
                                )
                                WeightInput(
                                    weight = uiState.weight,
                                    onWeightChange = viewModel::updateWeight,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        "BODYWEIGHT" -> {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                RepsInput(
                                    reps = uiState.reps,
                                    onRepsChange = viewModel::updateReps,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                BandsInput(
                                    bands = uiState.bands,
                                    onBandsChange = viewModel::updateBands,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                WeightInput(
                                    weight = uiState.weight,
                                    onWeightChange = viewModel::updateWeight,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        "MOVEMENT" -> {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                RepsInput(
                                    reps = uiState.reps,
                                    onRepsChange = viewModel::updateReps,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                DistanceInput(
                                    distance = uiState.distance,
                                    onDistanceChange = viewModel::updateDistance,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                BandsInput(
                                    bands = uiState.bands,
                                    onBandsChange = viewModel::updateBands,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                WeightInput(
                                    weight = uiState.weight,
                                    onWeightChange = viewModel::updateWeight,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = viewModel::addExerciseSet,
                        enabled = uiState.isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ajouter l'exercice")
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Chrono()
                    Timer()

                    // Liste des séries déjà ajoutées (inchangé)
                    if (uiState.exerciseSets.isNotEmpty()) {
                        Text(
                            "Exercices ajoutés (${uiState.exerciseSets.size})",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = DividerDefaults.Thickness,
                            color = DividerDefaults.color
                        )
                        uiState.exerciseSets.forEachIndexed { index, exerciseSet ->
                            val exercise = uiState.exercises.find { it.id == exerciseSet.exerciseId }
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "#${index + 1} - ${exercise?.name ?: "Exercice ${exerciseSet.exerciseId}"}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("${exerciseSet.repNumber} reps × ${exerciseSet.weight} kg")
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = viewModel::clearWorkout,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Effacer tout")
                            }

                            ExportJsonButton(
                                viewModel = viewModel,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Text(
                            "Aucun exercice ajouté. Sélectionnez un exercice pour commencer !",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}
