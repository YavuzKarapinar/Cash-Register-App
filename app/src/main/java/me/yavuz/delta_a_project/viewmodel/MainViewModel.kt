package me.yavuz.delta_a_project.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.yavuz.delta_a_project.database.repository.DepartmentRepository
import me.yavuz.delta_a_project.database.repository.GroupRepository
import me.yavuz.delta_a_project.database.repository.ProductRepository
import me.yavuz.delta_a_project.database.repository.SellingProcessRepository
import me.yavuz.delta_a_project.database.repository.TaxRepository
import me.yavuz.delta_a_project.database.repository.UserRepository
import me.yavuz.delta_a_project.model.Department
import me.yavuz.delta_a_project.model.Group
import me.yavuz.delta_a_project.model.Product
import me.yavuz.delta_a_project.model.SellingProcess
import me.yavuz.delta_a_project.model.SellingProcessType
import me.yavuz.delta_a_project.model.Tax
import me.yavuz.delta_a_project.model.User
import me.yavuz.delta_a_project.model.UserType

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    private val groupRepository = GroupRepository(application)
    private val departmentRepository = DepartmentRepository(application)
    private val taxRepository = TaxRepository(application)
    private val productRepository = ProductRepository(application)
    private val sellingProcessRepository = SellingProcessRepository(application)

    fun getUserByNameAndPassword(name: String, password: String): LiveData<User?> {
        val userLiveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val user = userRepository.getUserByNameAndPassword(name, password)
            userLiveData.postValue(user)
        }

        return userLiveData
    }

    suspend fun getUserById(id: Int): User? {
        return userRepository.getUserById(id)
    }

    fun saveUser(userTypeName: String, name: String, password: String) {
        viewModelScope.launch {
            userRepository.saveUser(userTypeName, name, password)
        }
    }

    fun getUsers(): LiveData<List<User>> {
        val usersLiveData = MutableLiveData<List<User>>()
        viewModelScope.launch {
            val users = userRepository.getUsers()
            usersLiveData.postValue(users)
        }
        return usersLiveData
    }

    fun getUserTypes(): LiveData<List<UserType>> {
        val userTypesLiveData = MutableLiveData<List<UserType>>()
        viewModelScope.launch {
            val userTypes = userRepository.getUserTypes()
            userTypesLiveData.postValue(userTypes)
        }
        return userTypesLiveData
    }

    fun getGroups(): LiveData<List<Group>> {
        val groupsLiveData = MutableLiveData<List<Group>>()
        viewModelScope.launch {
            val groups = groupRepository.getGroups()
            groupsLiveData.postValue(groups)
        }

        return groupsLiveData
    }

    fun saveGroup(name: String) {
        viewModelScope.launch {
            groupRepository.saveGroup(name)
        }
    }

    fun getDepartments(): LiveData<List<Department>> {
        val departmentsLiveData = MutableLiveData<List<Department>>()
        viewModelScope.launch {
            val departments = departmentRepository.getDepartments()
            departmentsLiveData.postValue(departments)
        }

        return departmentsLiveData
    }

    suspend fun getDepartmentByName(name: String): Department? {
        return departmentRepository.getDepartmentByName(name)
    }

    fun saveDepartment(group: String, name: String) {
        viewModelScope.launch {
            departmentRepository.saveDepartment(group, name)
        }
    }

    fun getTaxes(): LiveData<List<Tax>> {
        val taxesLiveData = MutableLiveData<List<Tax>>()
        viewModelScope.launch {
            val taxes = taxRepository.getTaxes()
            taxesLiveData.postValue(taxes)
        }

        return taxesLiveData
    }

    suspend fun getTaxByName(name: String): Tax? {
        return taxRepository.getTaxByName(name)
    }

    suspend fun getTaxById(id: Int): Tax? {
        return taxRepository.getTaxById(id)
    }

    fun saveTax(name: String, value: Double) {
        viewModelScope.launch {
            taxRepository.saveTax(name, value)
        }
    }

    fun getProducts(): LiveData<List<Product>> {
        val productsLiveData = MutableLiveData<List<Product>>()
        viewModelScope.launch {
            val products = productRepository.getProducts()
            productsLiveData.postValue(products)
        }
        return productsLiveData
    }

    suspend fun getProductById(id: Int): Product? {
        return productRepository.getProductById(id)
    }

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            productRepository.saveProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productRepository.updateProduct(product)
        }
    }

    suspend fun getSellingProcessById(id: Int): SellingProcess? {
        return sellingProcessRepository.getSellingProcessById(id)
    }

    suspend fun saveSellingProcess(sellingProcess: SellingProcess): Long {
        return sellingProcessRepository.saveSellingProcess(sellingProcess)
    }

    suspend fun getSellingTypeById(id: Int): SellingProcessType? {
        return sellingProcessRepository.getSellingTypeById(id)
    }
}