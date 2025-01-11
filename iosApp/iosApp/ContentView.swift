import SwiftUI
import GikuCampKit

struct ContentView: View {
	let storeOwner = SharedViewModelStoreOwner<DefinitionMenuViewModel>()

	var body: some View {
		Observing(storeOwner.instance.uiState) { uiState in
			VStack {
				Text(uiState.trainingMenu.id)
				Button("change") {
					storeOwner.instance.startTraining()
				}
			}
		}
	}
}

