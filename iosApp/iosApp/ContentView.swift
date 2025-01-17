import SwiftUI
import GikuCampKit

struct ContentView: View {
	@State var path = NavigationPath()
	var body: some View {
		NavigationStack(path: $path) {
			List {
				NavigationLink(TrainingPath.definitionMenu.toString, value: TrainingPath.definitionMenu)
				NavigationLink(TrainingPath.trainingWithCamera.toString, value: TrainingPath.trainingWithCamera)
			}
			.navigationTitle(TrainingPath.definitionMenu.toString)
			.navigationBarTitleDisplayMode(.inline)
			.navigationDestination(for: TrainingPath.self, destination: { appended in
				appended.Destination()
					.navigationTitle(appended.toString)
					.navigationBarTitleDisplayMode(.inline)
			})
		}
	}
}
