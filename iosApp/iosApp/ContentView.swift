import SwiftUI
import GikuCampKit

struct ContentView: View {
	@State var path = NavigationPath()
	var body: some View {
		NavigationView {
			MenuDefinitionScreen()
		}
	}
}
