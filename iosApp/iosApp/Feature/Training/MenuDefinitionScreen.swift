import SwiftUI
import GikuCampKit

class MenuDefinitionObservableState: ObservableObject {
    @Published var sets: String = ""
    @Published var reps: String = ""
    @Published var rest: String = ""
    @Published var weight: String = ""
}

struct MenuDefinitionScreen: View {
    let storeOwner = CustomViewModelStoreOwner<MenuDefinitionViewModel>()
    @StateObject private var observableState = MenuDefinitionObservableState()

    var body: some View {
        Observing(storeOwner.instance.uiState) { uiState in
            VStack {
                ScrollView {
                    TrainingMenuInputs(
                        observableState: observableState, uiState: uiState,
                        onSetsChanged: { storeOwner.instance.changeSets(sets: observableState.sets) },
                        onRepsChanged: { storeOwner.instance.changeReps(reps: observableState.reps) },
                        onWeightChanged: { storeOwner.instance.changeWeight(weight: observableState.weight) },
                        onRestChanged: { storeOwner.instance.changeRest(rest: observableState.rest) }
                    )
                    TrainingTypesByGroup(
                        muscleGroupsUiModel: uiState.muscleGroupsUiModel,
                        onSelectTrainingType: { selectedType in
                            storeOwner.instance.changeSets("3")
                            storeOwner.instance.changeReps("10")
                            storeOwner.instance.changeWeight("20.0")
                            storeOwner.instance.changeRest("120")
                            storeOwner.instance.changeTrainingType(type: selectedType)
                        }
                    )
                }
                .padding()
            }
        }
    }
}

struct TrainingMenuInputs: View {

    @ObservedObject var observableState: MenuDefinitionObservableState
    let uiState: DefinitionMenuUiState
    let onSetsChanged: () -> Void
    let onRepsChanged: () -> Void
    let onWeightChanged: () -> Void
    let onRestChanged: () -> Void


    var body: some View {
        VStack(spacing: 16) {
            HStack(spacing: 8) {
                TextField("Sets", text: $observableState.sets)
                    .keyboardType(.numberPad)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onChange(of: observableState.sets) { _ in
                        onSetsChanged()
                    }
                TextField("Reps", text: $observableState.reps)
                    .keyboardType(.numberPad)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onChange(of: observableState.reps) { _ in
                        onRepsChanged()
                    }
                TextField("Weight", text: $observableState.weight)
                    .keyboardType(.numberPad)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onChange(of: observableState.weight) { _ in
                        onWeightChanged()
                    }
            }

            HStack(spacing: 8) {
//                TextField(
//                    "Training Type",
//                    text: Binding(
//                        get: { uiState.selectedTrainingType?.name ?? "Select Training Type" },
//                        set: { _ in }
//                    )
//                )
//                    .disabled(true)
//                    .textFieldStyle(RoundedBorderTextFieldStyle())
                Text(uiState.selectedTrainingType?.name ?? "Select Type") // 値を表示
                    .font(.body)
                    .padding(8)
                    .background(
                        RoundedRectangle(cornerRadius: 5)
                            .stroke(Color.gray, lineWidth: 1)
                    )

                TextField("Rest", text: $observableState.rest)
                    .keyboardType(.numberPad)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onChange(of: observableState.rest) { _ in
                        onRestChanged()
                    }
            }
        }
    }
}

struct TrainingTypesByGroup: View {
    let muscleGroupsUiModel: MuscleGroupsUiModel
    let onSelectTrainingType: (TrainingType) -> Void

    var body: some View {
        ForEach(muscleGroupsUiModel.trainingTypesByGroup.keys.sorted {
            $0.id < $1.id
        }, id: \.self) { muscleGroup in
            VStack(alignment: .leading, spacing: 8) {
                Text(muscleGroup.name)
                    .font(.headline)
                ForEach(muscleGroupsUiModel.trainingTypesByGroup[muscleGroup] ?? [], id: \.id) { trainingType in
                    TrainingTypeItem(trainingType: trainingType, onSelectTrainingType: onSelectTrainingType)
                }
            }
            .padding(.vertical, 8)
        }
    }
}

struct TrainingTypeItem: View {
    let trainingType: TrainingType
    let onSelectTrainingType: (TrainingType) -> Void

    var body: some View {
        Button(action: {
            onSelectTrainingType(trainingType)
        }) {
            VStack(alignment: .leading) {
                Text(trainingType.name)
                    .font(.title3)
                Text(trainingType.description_)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            .padding()
            .background(Color.gray.opacity(0.1))
            .cornerRadius(8)
        }
    }
}
