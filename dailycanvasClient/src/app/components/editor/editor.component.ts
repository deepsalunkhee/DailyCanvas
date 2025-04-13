import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { animate, style, transition, trigger, state } from '@angular/animations';
import { FormsModule } from '@angular/forms';

interface Todo {
  id?: number;
  content: string;
  actionApplied: boolean;
  actionType: string; // "DONE" or "SCRATCHED" or "NONE"
  textColor: string; // "black" or "blue"
  fontSize: string; // "S", "M", "L"
  scratchColor: string; // "blue" or "black" or "none"
  position: number;
}

interface MenuOption {
  title: string;
  options: string[];
}

interface MenuConfig {
  [key: string]: MenuOption;
}

@Component({
  selector: 'app-editor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-10px)' }),
        animate('200ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' }))
      ])
    ]),
    trigger('strikeThrough', [
      state('NONE', style({ 
        textDecoration: 'none',
        position: 'relative'  // Add this to maintain position
      })),
      state('DONE', style({ 
        textDecoration: 'line-through',
        'text-decoration-color': 'green',
        'text-decoration-thickness': '2px',
        position: 'relative'  // Add this to maintain position
      })),
      state('SCRATCHED', style({ 
        textDecoration: 'line-through',
        'text-decoration-color': '{{color}}',
        'text-decoration-thickness': '2px',
        position: 'relative'  // Add this to maintain position
      }), { params: { color: 'black' }}),
      transition('* => *', [
        animate('0.3s ease-out')
      ])
    ])
  ]
})
export class EditorComponent implements OnInit {
  @Input() todos: Todo[] = [];
  @Output() saveTodo = new EventEmitter<Todo>();
  @Output() updateTodo = new EventEmitter<Todo>();
  @Output() deleteTodo = new EventEmitter<Todo>();
  
  newTodoContent: string = '';
  editingTodoId?: number;
  maxTodos = 20;
  
  editorState: {[key: string]: string} = {
    mode: 'write',
    actionType: 'done',
    fontColor: 'black',
    fontSize: 'M'
  };

  menuConfig: MenuConfig = {
    mode: {
      title: 'Editor Mode',
      options: ['write', 'action']
    },
    actionType: {
      title: 'Action Type',
      options: ['done', 'scratch']
    },
    fontColor: {
      title: 'Font Color',
      options: ['black', 'blue']
    },
    fontSize: {
      title: 'Font Size',
      options: ['S', 'M', 'L']
    }
  };

  sectionKeys: string[] = ['mode', 'actionType', 'fontColor', 'fontSize'];

  ngOnInit() {}

  updateState(section: string, value: string): void {
    if (section in this.editorState) {
      this.editorState[section] = value;
    }
  }

  isActive(category: string, value: string): boolean {
    return this.editorState[category] === value;
  }

  addTodo(): void {
    if (!this.newTodoContent.trim() || this.todos.length >= this.maxTodos) return;
  
    const newTodo: Todo = {
      id: Date.now(), // Generate a unique ID using timestamp
      content: this.newTodoContent,
      actionApplied: false,
      actionType: 'NONE',
      textColor: this.editorState['fontColor'],
      fontSize: this.editorState['fontSize'],
      scratchColor: 'none',
      position: this.todos.length
    };
  
    this.todos = [...this.todos, newTodo];
    this.saveTodo.emit(newTodo);
    this.newTodoContent = '';
  }

  editTodo(todo: Todo): void {
    if (this.editorState['mode'] !== 'write') return;
    
    this.editingTodoId = todo.id;
    this.newTodoContent = todo.content;
    this.updateState('fontColor', todo.textColor);
    this.updateState('fontSize', todo.fontSize);
  }

  updateTodoContent(): void {
    if (!this.editingTodoId || !this.newTodoContent.trim()) return;

    const todoIndex = this.todos.findIndex(t => t.id === this.editingTodoId);
    if (todoIndex === -1) return;

    const updatedTodo: Todo = {
      ...this.todos[todoIndex],
      content: this.newTodoContent,
      textColor: this.editorState['fontColor'],
      fontSize: this.editorState['fontSize']
    };

    // Update local array
    this.todos = [
      ...this.todos.slice(0, todoIndex),
      updatedTodo,
      ...this.todos.slice(todoIndex + 1)
    ];

    // Emit the update
    this.updateTodo.emit(updatedTodo);
    this.cancelEdit();
  }

  cancelEdit(): void {
    this.editingTodoId = undefined;
    this.newTodoContent = '';
  }

  deleteTask(todo: Todo, event: Event): void {
    event.stopPropagation();
    this.todos = this.todos.filter(t => t.id !== todo.id);
    this.deleteTodo.emit(todo);
  }

  getTodoClasses(todo: Todo): any {
    return {
      'todo-item': true,
      [`font-${todo.fontSize.toLowerCase()}`]: true,
      [`color-${todo.textColor}`]: true,
      'action-applied': todo.actionApplied,
      [todo.actionType.toLowerCase()]: todo.actionApplied
    };
  }

  getStrikeColor(todo: Todo): string {
    if (todo.actionType === 'SCRATCHED') {
      return todo.scratchColor !== 'none' ? todo.scratchColor : 'black';
    }
    return 'green';
  }

  trackById(index: number, todo: Todo): number {
    return todo.id || index; 
  }

  handleTodoClick(todo: Todo): void {
    if (this.editorState['mode'] === 'action') {
      this.applyAction(todo);
    } else if (this.editorState['mode'] === 'write') {
      this.editTodo(todo);
    }
  }

  applyAction(todo: Todo): void {
    const updatedTodo = {
      ...todo,
      actionApplied: todo.actionType === 'NONE',
      actionType: todo.actionType === 'NONE' 
        ? this.editorState['actionType'] === 'done' ? 'DONE' : 'SCRATCHED'
        : 'NONE',
      scratchColor: this.editorState['actionType'] === 'scratch' 
        ? this.editorState['fontColor'] 
        : 'none'
    };
  
    this.todos = this.todos.map(item => 
      item.id === todo.id ? updatedTodo : item
    );
    
    this.updateTodo.emit(updatedTodo);
  }
}