import SwiftUI
import shared
import Observation

struct ContentView: View {
	let viewModel = DefinitionMenuViewModel()

	var body: some View {
		Observing(viewModel.uiState) { uiState in
			Text("counter")
		}
	}
}
