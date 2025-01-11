import SwiftUI
import GikuCampKit

struct ContentView: View {
	let storeOwner = CustomViewModelStoreOwner<DefinitionMenuViewModel>()

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
