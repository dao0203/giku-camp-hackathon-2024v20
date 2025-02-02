import Foundation
import GikuCampKit

class CustomViewModelStoreOwner<VM: ViewModel> : ObservableObject, ViewModelStoreOwner {
    var viewModelStore: ViewModelStore = ViewModelStore()

    private let key: String = String(describing: type(of: VM.self))

    init(_ viewModel: VM = .init()) {
        viewModelStore.put(key: key, viewModel: viewModel)
    }

    var instance: VM {
        get {
            viewModelStore.get(key: key) as! VM
        }
    }

    deinit {
        viewModelStore.clear()
    }
}
