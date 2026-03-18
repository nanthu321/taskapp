

document.addEventListener("DOMContentLoaded", () => {
	
	const user = JSON.parse(localStorage.getItem("user"));
    const currentPath = window.location.pathname;
    const isPublicPage = currentPath.endsWith("login.html") || currentPath.endsWith("signup.html") || currentPath.endsWith("index.html") || currentPath.endsWith("/");

    if (!user && !isPublicPage) {
        window.location.replace("login.html");
        return;
    } else if (user && isPublicPage) {
        window.location.replace("dashboard.html"); 
        return; 
    }

    if (!isPublicPage) {
        loadTasks(); 
    }

	
    //@ Signup elements
    const registerForm = document.getElementById("registerForm");
    const submitBtn = document.getElementById("registerBtn")
    const regAlertBox = document.getElementById("registerAlert");

    //@ Login elements
    const loginForm = document.getElementById("loginForm");
    const loginBtn = document.getElementById("loginBtn");
    const loginAlertBox = document.getElementById("loginAlert");


    //@ Signup Process
    registerForm?.addEventListener("submit", function(e) {
        e.preventDefault();


        submitBtn.disabled = true;
        submitBtn.textContent = "Creating...";

        const username = document.getElementById("regUsername").value.trim();
        const email = document.getElementById("regEmail").value.trim();
        const password = document.getElementById("regPassword").value.trim();
        const confirmPassword = document.getElementById("regConfirmPassword").value.trim();

        if (!password) {
            showAlert("Passwords do not be empty", "error", regAlertBox);
            submitBtn.disabled = false;
            submitBtn.textContent = "Create Account";
            return;
        }

        if (password !== confirmPassword) {
            showAlert("Passwords do not match", "error", regAlertBox);
            submitBtn.disabled = false;
            submitBtn.textContent = "Create Account";
            return;
        }
        if (!username) {
            showAlert("User name require", "error", regAlertBox);
            submitBtn.disabled = false;
            submitBtn.textContent = "Create Account";
            return;
        }

        const formData = new URLSearchParams();



        formData.append("username", username);
        formData.append("email", email);
        formData.append("password", password);

        fetch("/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData
        })
            .then(response => response.text())
            .then(data => {

                if (data === "success") {
                    showAlert("Account created successfully!", "success");

                    setTimeout(() => {
                        window.location.href = "login.html";
                    }, 1500);

                } else if (data === "email_exists") {
                    showAlert("Email already registered", "error");
                } else {
                    showAlert("Server error", "error");
                }

                submitBtn.disabled = false;
                submitBtn.textContent = "Create Account";

            })
            .catch(error => {
				console.error("Signup error:", error);
                showAlert("Server error. Please try again.", "error", regAlertBox);
                submitBtn.disabled = false;
                submitBtn.textContent = "Create Account";
            });

    });



   
	// @ Login process
	loginForm?.addEventListener("submit", function (e) {
	    e.preventDefault();
		const sidebarUsername = document.getElementById("sidebarUsername");
		const sidebarEmail = document.getElementById("sidebarEmail");
		
	    loginBtn.disabled = true;
	    loginBtn.textContent = "Logging in...";

	    const email = document.getElementById("loginEmail").value.trim();
	    const password = document.getElementById("loginPassword").value.trim();

	    if (!password) {
	        showAlert("Password cannot be empty", "error", loginAlertBox);
	        loginBtn.disabled = false;
	        loginBtn.textContent = "Login";
	        return;
	    }

	    const formData = new URLSearchParams();
	    formData.append("email", email);
	    formData.append("password", password);
		
	    fetch("/login", {
	        method: "POST",
	        headers: {
	            "Content-Type": "application/x-www-form-urlencoded"
	        },
	        body: formData
	    })
	        .then(response => response.json())   
	        .then(data => {

	            if (data && data.userId) {
	                showAlert("Login successfully...", "success", loginAlertBox);
					localStorage.setItem("user", JSON.stringify(data));
					window.location.href = "dashboard.html";					
	            } else {
	                showAlert("Invalid credential", "error", loginAlertBox);
	            }

	            loginBtn.textContent = "Login";
	            loginBtn.disabled = false;
				
				
	        })
	        .catch(error => {
	            console.error(error);
	            showAlert("Server Error. Please try again.", "error", loginAlertBox);

	            loginBtn.disabled = false;
	            loginBtn.textContent = "Login";
	        });
	});


});





document.getElementById('taskForm')?.addEventListener('submit', function(e) {
    e.preventDefault();

    const taskId = document.getElementById('taskId').value;
    const title = document.getElementById('taskTitle').value;
    const description = document.getElementById('taskDescription').value;
	console.log(taskId, title,description);

    if(taskId) {
        updateTask(taskId, title, description);
    } else {
        addTask(title, description);
    }

    closeTaskModal();
});





function addTask(title, description){
	const formData = new URLSearchParams();
	formData.append("title", title);
	formData.append("description", description);
	fetch("/tasks", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formData
	    })
	   	.then(res => res.text())
	    .then(data => {

	    if (data === "success") {
			showToast('Task Added Successfully', 'info');
			loadTasks();
	    }
    });
}


function updateTask(taskId, title, description) {

    fetch("/tasks", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            taskId: taskId,
            title: title,
            description: description
        })
    })
    .then(res => res.text())
    .then(data => {
        if (data === "success") {
            loadTasks();
			showToast('Task Updated.', 'info');
        }
    });
}


var userTasks;

//@ This is for load the created task based on the login user
function loadTasks() {	
	const totalTasks = document.getElementById("totalTasks");
	const completedTasks = document.getElementById("completedTasks");
	const pendingTasks = document.getElementById("pendingTasks");
	const taskCount = document.getElementById("taskCount");
	
    fetch("/tasks")
        .then(res => res.json())
        .then(tasks => {
			userTasks = tasks;
			console.log("User tasks : "+userTasks);
            //const grid = document.getElementById("taskGrid");
             

			totalTasks.innerText = tasks.length;
			pendingTasks.innerText = tasks.filter((task)=>task.status=="pending").length;
			completedTasks.innerText = tasks.filter((task)=>task.status=="completed").length;
			taskCount.innerText = `${tasks.length} Tasks`
			
			renderTask(tasks);
        });
}



function showAlert(message, type, alertBox) {
    alertBox.style.display = "block";
    alertBox.textContent = message;

    if (type === "success") {
        alertBox.style.background = "#d4edda";
        alertBox.style.color = "#155724";
    } else {
        alertBox.style.background = "#f8d7da";
        alertBox.style.color = "#721c24";
    }
}



//@ This is for show and hide the password value
function togglePasswordVisibility(inputId, btn) {
    const input = document.getElementById(inputId);
    const icon = btn.querySelector('i');

    if (input.type === 'password') {
        input.type = 'text';
        icon.className = 'fas fa-eye-slash';
    } else {
        input.type = 'password';
        icon.className = 'fas fa-eye';
    }
}


function openTaskModal() {
    const modal = document.getElementById('taskModal');
    const modalTitle = document.getElementById('modalTitle');
    const taskIdField = document.getElementById('taskId');
    const taskTitleInput = document.getElementById('taskTitle');
    const taskDescInput = document.getElementById('taskDescription');
    const submitBtn = document.getElementById('taskSubmitBtn');

    // Clear form
    taskIdField.value = '';
    taskTitleInput.value = '';
    taskDescInput.value = '';

    // Set modal to "Add" mode
    modalTitle.innerHTML = '<i class="fas fa-plus-circle"></i> Add New Task';
    submitBtn.innerHTML = '<i class="fas fa-save"></i> Save Task';

    // Show modal with animation
    modal.classList.add('active');

    // Focus the title input
    setTimeout(() => taskTitleInput.focus(), 100);
}


function closeTaskModal() {
    const modal = document.getElementById('taskModal');
    modal.classList.remove('active');
}


function formatDate(dateString) {
    const date = new Date(dateString);
    const options = {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
    };
    return date.toLocaleDateString('en-US', options);
}


function handleEditTask(taskId) {
	console.log("edit button clicked")
   
    const tasks = userTasks;
    const task = tasks.find(t => t.taskId === taskId);
	console.log("selected task : "+task);
    if (!task) return;

    const modal = document.getElementById('taskModal');
    const modalTitle = document.getElementById('modalTitle');
    const taskIdField = document.getElementById('taskId');
    const taskTitleInput = document.getElementById('taskTitle');
    const taskDescInput = document.getElementById('taskDescription');
    const submitBtn = document.getElementById('taskSubmitBtn');

    
	// Pre-fill form with existing task data
    taskIdField.value = task.taskId;
    taskTitleInput.value = task.title;
    taskDescInput.value = task.description;

    // Set modal to "Edit" mode
    modalTitle.innerHTML = '<i class="fas fa-edit"></i> Edit Task';
    submitBtn.innerHTML = '<i class="fas fa-save"></i> Update Task';

    // Show modal
    modal.classList.add('active');

    // Focus the title input
    setTimeout(() => taskTitleInput.focus(), 100);
}


function handleDeleteTask(taskId) {
    const deleteModal = document.getElementById('deleteModal');
    const deleteTaskIdField = document.getElementById('deleteTaskId');

    deleteTaskIdField.value = taskId;
    deleteModal.classList.add('active');
}


function closeDeleteModal() {
    const deleteModal = document.getElementById('deleteModal');
    deleteModal.classList.remove('active');
}

function confirmDeleteTask() {
    
    const taskId = document.getElementById('deleteTaskId').value;
	
    if (taskId) {
        deleteTask(taskId);
        loadTasks();
        showToast('Task deleted.', 'error');
    }

    closeDeleteModal();
}

function deleteTask(taskId){
    fetch(`/tasks?taskId=${taskId}`, {
        method: "DELETE"
    })
    .then(res => res.text())
    .then(data => {
        if (data === "success") {
            loadTasks();
        }
    });
}

function handleToggleTask(taskId) {
	
	const tasks = userTasks;
    const task = tasks.find(t => t.taskId === taskId);

    const wasCompleted = task && task.status === 'completed';
	

    if (wasCompleted) {
		toggleTaskStatus(taskId,"pending");
        showToast('Task marked as pending.', 'info');
    } else {
		toggleTaskStatus(taskId,"completed");
        showToast('Task completed! Great job! 🎉', 'success');
    }
	loadTasks();
}

function toggleTaskStatus(taskId,status){
	
	const formData = new URLSearchParams();
	
	formData.append("taskId",taskId);
	formData.append("status",status);
	formData.append("action","toggle");
	
	fetch("/tasks", {
	    method: "POST",
	    headers: {
	        "Content-Type": "application/x-www-form-urlencoded"
	    },
	    body: formData
	    })
	   	.then(res => res.text())
	    .then(data => {
	
	    if (data === "success") {
			loadTasks();
	    }
    });
}


function showToast(message, type = 'info') {
    const container = document.querySelector('.toast-container');
    if (!container) return;

    const icons = {
        success: 'fas fa-check-circle',
        error: 'fas fa-trash-alt',
        info: 'fas fa-info-circle'
    };

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `<i class="${icons[type]}"></i> ${message}`;

    container.appendChild(toast);

    
    setTimeout(() => {
        if (toast.parentNode) {
            toast.parentNode.removeChild(toast);
        }
    }, 3000);
}


function filterTask(value){
	let filteredTask;
	if(value == "pending")
		filteredTask = userTasks.filter(t => t.status === 'pending');
	else if(value == "completed")
		filteredTask = userTasks.filter(t => t.status === 'completed');
	else if(value == "all")
		filteredTask = userTasks;
	
	const grid = document.getElementById("taskGrid");
	
	if (filteredTask.length === 0) {
	       const emptyMessage =  value !== 'all' ? `No ${value} tasks found.` : 'No tasks yet';
		   const emptySubtext = 'Click "Add Task" to create your first task and get started!';

	       grid.innerHTML = `
	           <div class="empty-state">
			   		<div class="empty-icon">
                    	<i class="fas fa-clipboard-list"></i>
   	                </div>
	               <h3>${emptyMessage}</h3>
	               <p>${emptySubtext}</p>
	           </div>
	       `;
	       return;
	  }
	
		
	renderTask(filteredTask);
}

function searchTasks(value) {
    let filteredTasks = userTasks.filter(t => 
        t.title.toLowerCase().includes(value.toLowerCase())
    );
	const grid = document.getElementById("taskGrid");
	
	if (filteredTasks.length === 0) {
	        const emptyMessage = 'No tasks match your search.'
	          
	        const emptySubtext = 'Try a different search term.'
	          
	        grid.innerHTML = `
	            <div class="empty-state">
	                <div class="empty-icon">
	                    <i class="fas fa-search"></i>
	                </div>
	                <h3>${emptyMessage}</h3>
	                <p>${emptySubtext}</p>
	               
                    <button class="btn btn-primary" onclick="openTaskModal()">
                        <i class="fas fa-plus"></i> Create First Task
                    </button>
	                
	            </div>
	        `;
	        return;
	 }	
	renderTask(filteredTasks);

}

function renderTask(tasks){
	const grid = document.getElementById("taskGrid");
	grid.innerHTML = "";  
	let html = ""; 

    
	tasks.forEach(task => {
        const isCompleted = task.status === "completed";
        
        html += `
            <div class="task-card ${isCompleted ? "completed" : ""}" data-task-id="${task.taskId}">
                <div class="task-card-header">
                    <span class="task-status-badge ${isCompleted ? "badge-completed" : "badge-pending"}">
                        <i class="fas ${isCompleted ? "fa-check-circle" : "fa-clock"}"></i>
                        ${isCompleted ? "Completed" : "Pending"}
                    </span>
                    <div class="task-card-actions">
                        <button class="task-action-btn btn-complete" title="${isCompleted ? "Mark as Pending" : "Mark as Completed"}" onclick="handleToggleTask(${task.taskId})">
                            <i class="fas ${isCompleted ? "fa-undo" : "fa-check"}"></i>
                        </button>
                        <button class="task-action-btn btn-edit" title="Edit Task" onclick="handleEditTask(${task.taskId})">
                            <i class="fas fa-pen"></i>
                        </button>
                        <button class="task-action-btn btn-delete" title="Delete Task" onclick="handleDeleteTask(${task.taskId})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
                <h3 class="task-card-title">${task.title}</h3>
                <p class="task-card-description">${task.description || ""}</p>
                <div class="task-card-footer">
                    <i class="far fa-calendar-alt"></i>
                    <span>Created ${formatDate(task.createdAt)}</span>
                </div>
            </div>
        `;
    });

    grid.innerHTML = html;  
}




function logoutUser() {
    fetch("/logout")
        .then(() => {
            localStorage.clear(); 
            window.location.replace("login.html");
        });
}


function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    sidebar.classList.toggle('open');
    overlay.classList.toggle('open');
}





function initDashboard() {
	const user = JSON.parse(localStorage.getItem("user"));
	
    const usernameEl = document.getElementById('sidebarUsername');
    const emailEl = document.getElementById('sidebarEmail');
    const avatarEl = document.getElementById('userAvatar');
    const greetingEl = document.getElementById('dashboardGreeting');

    if (usernameEl) usernameEl.textContent = user.username;
    if (emailEl) emailEl.textContent = user.email;
    if (avatarEl) avatarEl.textContent = user.username.charAt(0).toUpperCase();
    if (greetingEl) greetingEl.textContent = `${getGreeting()}, ${user.username}!`;

   
    loadTasks();
}

function getGreeting() {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good Morning';
    if (hour < 18) return 'Good Afternoon';
    return 'Good Evening';
}
