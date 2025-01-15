import SwiftUI
import GikuCampKit

struct DefinitionMenuScreen: View {
	let storeOwner = CustomViewModelStoreOwner<DefinitionMenuViewModel>()

	var body: some View {
		Observing(storeOwner.instance.uiState) { uiState in
			VStack {
				List {
                    Section(header: Text("Training Menu")) {
                        TrainingMenuInputs(uiState: uiState)
                    }
                    Section(header: Text("Training Types")) {
                        ForEach(uiState.muscleGroupsUiModel.trainingTypesByGroup.keys.sorted(by: { $0.name < $1.name })) { muscleGroup in
                            Text(muscleGroup.name)
                            ForEach(uiState.muscleGroupsUiModel.trainingTypesByGroup[muscleGroup] ?? []) { trainingType in
                                TrainingTypeItem(trainingType: trainingType) {
                                    storeOwner.instance.changeTrainingType($0)
                                }
                            }
                        }
                    }
                }.listStyle(InsetGroupedListStyle)
			}
		}
	}
}
