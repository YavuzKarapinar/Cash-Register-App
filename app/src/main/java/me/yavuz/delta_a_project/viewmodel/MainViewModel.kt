package me.yavuz.delta_a_project.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.yavuz.delta_a_project.database.repository.DepartmentRepository
import me.yavuz.delta_a_project.database.repository.GroupRepository
import me.yavuz.delta_a_project.database.repository.ProductRepository
import me.yavuz.delta_a_project.database.repository.ReportRepository
import me.yavuz.delta_a_project.database.repository.SellingProcessRepository
import me.yavuz.delta_a_project.database.repository.TaxRepository
import me.yavuz.delta_a_project.database.repository.UserRepository
import me.yavuz.delta_a_project.model.Department
import me.yavuz.delta_a_project.model.Group
import me.yavuz.delta_a_project.model.Product
import me.yavuz.delta_a_project.model.ReportX
import me.yavuz.delta_a_project.model.ReportZ
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
    private val reportRepository = ReportRepository(application)

    /**
     * Getting user's live data info by name and password
     *
     * @param name name of the user
     * @param password password of the user
     *
     * @return [LiveData] list of nullable [User]
     */
    fun getUserByNameAndPassword(name: String, password: String): LiveData<User?> {
        val userLiveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val user = userRepository.getUserByNameAndPassword(name, password)
            userLiveData.postValue(user)
        }

        return userLiveData
    }

    /**
     * Getting user by user id
     *
     * @param id User id
     *
     * @return [User] as a nullable value
     */
    suspend fun getUserById(id: Int): User? {
        return userRepository.getUserById(id)
    }

    /**
     * Getting all users that in the database as a live data
     *
     * @return [LiveData] list of [User]
     */
    fun getUsers(): LiveData<List<User>> {
        val usersLiveData = MutableLiveData<List<User>>()
        viewModelScope.launch {
            val users = userRepository.getUsers()
            usersLiveData.postValue(users)
        }
        return usersLiveData
    }

    /**
     * Deleting user from database if user does not have any constraint exception
     *
     * @param user User instance
     * @param onSuccess lambda expression when deleting user is successful
     * @param onError lambda expression when deleting user is throws an error takes exception as a lambda parameter
     *
     * @throws SQLiteConstraintException throws exception if user has a foreign key constraint
     */
    fun deleteUser(user: User, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(user)
                onSuccess()
            } catch (e: SQLiteConstraintException) {
                onError(e)
            }
        }
    }

    /**
     * Updates user information
     *
     * @param user user that will updated.
     */
    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    /**
     * Saves user based on the [UserType]
     *
     * @param userTypeName takes user type name as a string
     * @param name user name as a string. User name should be **unique**
     * @param password password as a string. Password should be only integer values after that cast as a string
     */
    fun saveUser(userTypeName: String, name: String, password: String) {
        viewModelScope.launch {
            userRepository.saveUser(userTypeName, name, password)
        }
    }

    /**
     * Checks if user already in the database
     *
     * @param name user name.
     *
     * @return [Boolean] if true user exist if not user is not exist in the database
     */
    fun isUserExists(name: String): Boolean {
        return userRepository.isUserExists(name)
    }

    /**
     * Getting all user types as a [LiveData]
     *
     * @return [LiveData] list of [UserType]
     */
    fun getUserTypes(): LiveData<List<UserType>> {
        val userTypesLiveData = MutableLiveData<List<UserType>>()
        viewModelScope.launch {
            val userTypes = userRepository.getUserTypes()
            userTypesLiveData.postValue(userTypes)
        }
        return userTypesLiveData
    }

    /**
     * Getting Groups as a [LiveData]
     *
     * @return [LiveData] list of [Group]
     */
    fun getGroups(): LiveData<List<Group>> {
        val groupsLiveData = MutableLiveData<List<Group>>()
        viewModelScope.launch {
            val groups = groupRepository.getGroups()
            groupsLiveData.postValue(groups)
        }

        return groupsLiveData
    }

    /**
     * Getting group by group id
     *
     * @param id group id
     *
     * @return [Group] as a nullable
     */
    suspend fun getGroupById(id: Int): Group? {
        return groupRepository.getGroupById(id)
    }

    /**
     * Checks if group already in the database
     *
     * @param name group name.
     *
     * @return [Boolean] if true group exist if not group is not exist in the database
     */
    fun isGroupExists(name: String): Boolean {
        return groupRepository.isGroupExists(name)
    }

    /**
     * Saves group based on the group name
     *
     * @param name name of the group. Should be **unique**.
     */
    fun saveGroup(name: String) {
        viewModelScope.launch {
            groupRepository.saveGroup(name)
        }
    }

    /**
     * Updates group
     *
     * @param group instance of [Group]
     */
    fun updateGroup(group: Group) {
        viewModelScope.launch {
            groupRepository.updateGroup(group)
        }
    }

    /**
     * Deleting group from database if group does not have any constraint exception
     *
     * @param group instance of [Group]
     * @param onSuccess lambda expression when deleting group is successful
     * @param onError lambda expression when deleting group is throws an error takes exception as a lambda parameter
     *
     * @throws SQLiteConstraintException throws exception if group has a foreign key constraint
     */
    fun deleteGroup(group: Group, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                groupRepository.deleteGroup(group)
                onSuccess()
            } catch (e: SQLiteConstraintException) {
                onError(e)
            }
        }
    }

    /**
     * Getting departments as a [LiveData]
     *
     * @return [LiveData] list of [Department]
     */
    fun getDepartments(): LiveData<List<Department>> {
        val departmentsLiveData = MutableLiveData<List<Department>>()
        viewModelScope.launch {
            val departments = departmentRepository.getDepartments()
            departmentsLiveData.postValue(departments)
        }

        return departmentsLiveData
    }

    /**
     * Getting department by department name
     *
     * @param name Department name
     *
     * @return [Department] as a nullable value
     */
    suspend fun getDepartmentByName(name: String): Department? {
        return departmentRepository.getDepartmentByName(name)
    }

    /**
     * Getting department by department id
     *
     * @param id Department id
     *
     * @return [Department] as a nullable value
     */
    suspend fun getDepartmentById(id: Int): Department? {
        return departmentRepository.getDepartmentById(id)
    }

    /**
     * Checks if department in the database
     *
     * @param name Department name
     *
     * @return [Boolean] if true department exist if not department is not exist in the database
     */
    fun isDepartmentExists(name: String): Boolean {
        return departmentRepository.isDepartmentExists(name)
    }

    /**
     * Saves department based on the [Group] name
     *
     * @param group Group name
     * @param name Department name. Should be **unique**.
     */
    fun saveDepartment(group: String, name: String) {
        viewModelScope.launch {
            departmentRepository.saveDepartment(group, name)
        }
    }

    /**
     * Deleting department from database if department does not have any constraint exception
     *
     * @param department instance of [Department]
     * @param onSuccess lambda expression when deleting department is successful
     * @param onError lambda expression when deleting department throws an error takes exception as a lambda parameter
     *
     * @throws SQLiteConstraintException throws exception if department has a foreign key constraint
     */
    fun deleteDepartment(
        department: Department,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                departmentRepository.deleteDepartment(department)
                onSuccess()
            } catch (e: SQLiteConstraintException) {
                onError(e)
            }
        }
    }

    /**
     * Updates group
     *
     * @param department instance of [Department]
     */
    fun updateDepartment(department: Department) {
        viewModelScope.launch {
            departmentRepository.updateDepartment(department)
        }
    }

    /**
     * Getting taxes as a [LiveData]
     *
     * @return [LiveData] list of [Tax]
     */
    fun getTaxes(): LiveData<List<Tax>> {
        val taxesLiveData = MutableLiveData<List<Tax>>()
        viewModelScope.launch {
            val taxes = taxRepository.getTaxes()
            taxesLiveData.postValue(taxes)
        }

        return taxesLiveData
    }

    /**
     * Getting tax by its name
     *
     * @param name tax name
     *
     * @return [Tax] as a nullable value
     */
    suspend fun getTaxByName(name: String): Tax? {
        return taxRepository.getTaxByName(name)
    }

    /**
     * Getting tax by its id
     *
     * @param id tax id
     *
     * @return [Tax] as a nullable value
     */
    fun getTaxById(id: Int): Tax? {
        return taxRepository.getTaxById(id)
    }

    /**
     * Checks if tax in the database
     *
     * @param name Tax name
     *
     * @return [Boolean] if true tax exist if not tax is not exist in the database
     */
    fun isTaxExists(name: String): Boolean {
        return taxRepository.isTaxExists(name)
    }

    /**
     * Saves tax with its name and value
     *
     * @param name tax name. Should be **unique**.
     * @param value tax value. Should be double and must bigger than 0.0 and lower than 100.0
     */
    fun saveTax(name: String, value: Double) {
        viewModelScope.launch {
            taxRepository.saveTax(name, value)
        }
    }

    /**
     * Updates tas
     *
     * @param tax instance of [Tax]
     */
    fun updateTax(tax: Tax) {
        viewModelScope.launch {
            taxRepository.updateTax(tax)
        }
    }

    /**
     * Deleting tax from database if tax does not have any constraint exception
     *
     * @param tax instance of [Tax]
     * @param onSuccess lambda expression when deleting tax is successful
     * @param onError lambda expression when deleting tax throws an error takes exception as a lambda parameter
     *
     * @throws SQLiteConstraintException throws exception if tax has a foreign key constraint
     */
    fun deleteTax(tax: Tax, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                taxRepository.deleteTax(tax)
                onSuccess()
            } catch (e: SQLiteConstraintException) {
                onError(e)
            }
        }
    }

    /**
     * Getting products as a [LiveData]
     *
     * @return [LiveData] list of [Product]
     */
    fun getProducts(): LiveData<List<Product>> {
        val productsLiveData = MutableLiveData<List<Product>>()
        viewModelScope.launch {
            val products = productRepository.getProducts()
            productsLiveData.postValue(products)
        }
        return productsLiveData
    }

    /**
     * Getting product by its id
     *
     * @param id Product id
     *
     * @return [Product] as a nullable value
     */
    suspend fun getProductById(id: Int): Product? {
        return productRepository.getProductById(id)
    }

    /**
     * Getting [Product] by using its product number
     *
     * @param productNumber product number of [Product]
     *
     * @return [Product] as a nullable value
     */
    suspend fun getProductByProductNumber(productNumber: Int): Product? {
        return productRepository.getProductByProductNumber(productNumber)
    }

    /**
     * Checks if product in the database
     *
     * @param name Product name
     *
     * @return [Boolean] if true product exist if not product is not exist in the database
     */
    fun isProductExists(name: String): Boolean {
        return productRepository.isProductExists(name)
    }

    /**
     * Saves product
     *
     * @param product instance of [Product]
     */
    fun saveProduct(product: Product) {
        viewModelScope.launch {
            productRepository.saveProduct(product)
        }
    }

    /**
     * Deleting product from database if product does not have any constraint exception
     *
     * @param product instance of [Product]
     * @param onSuccess lambda expression when deleting product is successful
     * @param onError lambda expression when deleting product throws an error takes exception as a lambda parameter
     *
     * @throws SQLiteConstraintException throws exception if product has a foreign key constraint
     */
    fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                productRepository.deleteProduct(product)
                onSuccess()
            } catch (e: SQLiteConstraintException) {
                onError(e)
            }
        }
    }

    /**
     * Updates product
     *
     * @param product instance of [Product]
     */
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productRepository.updateProduct(product)
        }
    }

    /**
     * Getting [LiveData] of [SellingProcess] based on [ReportZ] id
     *
     * @param zId [ReportZ] id
     *
     * @return [LiveData] list of [SellingProcess]
     */
    fun getSellingProcessesByZReportId(zId: Int): LiveData<List<SellingProcess>> {
        val sellingProcessLiveData = MutableLiveData<List<SellingProcess>>()
        viewModelScope.launch {
            val sellingProcess = sellingProcessRepository.getSellingProcessesByZReportId(zId)
            sellingProcessLiveData.postValue(sellingProcess)
        }

        return sellingProcessLiveData
    }

    /**
     * Getting [LiveData] of [SellingProcess] based on [ReportZ] id and [ReportX] id
     *
     * @param xId [ReportX] id
     * @param zId [ReportZ] id
     *
     * @return [LiveData] list of [SellingProcess]
     */
    suspend fun getSellingProcessListByXAndZId(xId: Int, zId: Int): LiveData<List<SellingProcess>> {
        val sellingProcessLiveData = MutableLiveData<List<SellingProcess>>()
        viewModelScope.launch {
            val sellingProcess = sellingProcessRepository.getSellingProcessListByXAndZId(xId, zId)
            sellingProcessLiveData.postValue(sellingProcess)
        }

        return sellingProcessLiveData
    }

    /**
     * Saves selling process
     *
     * @param sellingProcess instance of [SellingProcess]
     *
     * @return returns database id as a [Long] value
     */
    suspend fun saveSellingProcess(sellingProcess: SellingProcess): Long {
        return sellingProcessRepository.saveSellingProcess(sellingProcess)
    }

    /**
     * Getting [SellingProcessType] by its id
     *
     * @param id id of [SellingProcessType]
     *
     * @return [SellingProcessType] as a nullable value
     */
    suspend fun getSellingTypeById(id: Int): SellingProcessType? {
        return sellingProcessRepository.getSellingTypeById(id)
    }

    /**
     * Getting last [ReportZ] id. Gets Last [ReportZ] id if there is no row in the database it will
     * add row with id of 1 and that moments timestamp
     *
     * @return id of the last [ReportZ]
     */
    suspend fun getLastZNumber(): Int {
        return reportRepository.getLastZNumber()
    }

    /**
     * Getting all [ReportZ] as a [LiveData]
     *
     * @return [LiveData] list of [ReportZ]
     */
    fun getAllReportZ(): LiveData<List<ReportZ>> {
        val reportZLiveData = MutableLiveData<List<ReportZ>>()
        viewModelScope.launch {
            val reportZ = reportRepository.getAllReportZ()
            reportZLiveData.postValue(reportZ)
        }

        return reportZLiveData
    }

    /**
     * Inserting new [ReportZ] to the database
     *
     * @return id of the inserted [ReportZ]
     */
    suspend fun insertReportZ(): Int {
        return reportRepository.insertReportZ()
    }

    /**
     * Getting last [ReportX] id. Gets Last [ReportX] id if there is no row in the database it will
     * add row with id of 1 and that moments timestamp
     *
     * @return id of the last [ReportX]
     */
    suspend fun getLastXNumber(): Int {
        return reportRepository.getLastXNumber()
    }

    /**
     * Getting all of the [ReportX] as a [LiveData]
     *
     * @return [LiveData] list of [ReportX]
     */
    fun getAllReportX(): LiveData<List<ReportX>> {
        val reportXLiveData = MutableLiveData<List<ReportX>>()
        viewModelScope.launch {
            val reportX = reportRepository.getAllReportX()
            reportXLiveData.postValue(reportX)
        }

        return reportXLiveData
    }

    /**
     * Inserting [ReportX] to the database based on the [ReportZ] id
     *
     * @param zId [ReportZ] id
     *
     * @return inserted [ReportX] id
     */
    suspend fun insertReportX(zId: Int): Int {
        return reportRepository.insertReportX(zId)
    }
}