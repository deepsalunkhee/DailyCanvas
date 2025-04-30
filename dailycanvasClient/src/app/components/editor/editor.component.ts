import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { animate, style, transition, trigger, state } from '@angular/animations';
import { FormsModule } from '@angular/forms';

interface Todo {
  id?: string;
  dayId: string;
  content: string;
  actionApplied: boolean;
  actionType: string | null; // "DONE" or "SCRATCHED" or null
  textColor: string; // "black" or "blue"
  fontSize: string; // "S", "M", "L"
  scratchColor: string | null; // "blue" or "black" or null
  position: number;
}

interface DayData {
  day: string;
  dayId: string;
  todos: Todo[];
}

interface WeekData {
  weekId: string;
  weekStartDate: string;
  days: DayData[];
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
      state('null', style({
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
      }), { params: { color: 'black' } }),
      transition('* => *', [
        animate('0.3s ease-out')
      ])
    ])
  ]
})
export class EditorComponent implements OnInit, OnChanges {
  @Input() weekData!: any;
  @Output() saveTodo = new EventEmitter<Todo>();
  @Output() updateTodo = new EventEmitter<Todo>();
  @Output() deleteTodo = new EventEmitter<Todo>();

  currentDayIndex: number = 0;
  days: DayData[] = [];
  currentDayTodos: Todo[] = [];

  newTodoContent: string = '';
  editingTodoId?: string;
  maxTodos = 20;

  editorState: { [key: string]: string } = {
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

  fontCharLimits: { [key: string]: number } = {
    S: 42, 
    M: 26,
    L: 12  
  };

  remainingChars: number = 0;
  

  // Define standard order of days
  private readonly standardDayOrder = [
    'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'
  ];

  ngOnInit() {
    // Initialize days array to avoid undefined errors
    this.days = [];
    this.currentDayTodos = [];
    this.remainingChars = this.fontCharLimits[this.editorState['fontSize']]; 
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['weekData'] && changes['weekData'].currentValue) {
      console.log('Week Data in editor:', this.weekData);

      // Safely access days array
      if (this.weekData && this.weekData.days && Array.isArray(this.weekData.days)) {
        // Sort days according to standard order (Sunday to Saturday)
        this.sortDaysInOrder();
        this.setTodayAsDefaultDay();
        this.loadCurrentDayTodos();

      } else {
        console.error('Invalid weekData structure:', this.weekData);
        this.days = [];
        this.currentDayTodos = [];
      }
    }
  }

  // Sort days in standard Sunday to Saturday order
  sortDaysInOrder(): void {
    if (!this.weekData || !this.weekData.days || !Array.isArray(this.weekData.days)) return;

    // Create a map for quick access to day data by day name
    const dayMap = new Map<string, DayData>();
    this.weekData.days.forEach((day: DayData) => {
      dayMap.set(day.day, day);
    });

    // Create a new ordered array based on standard day order
    const orderedDays: DayData[] = [];
    this.standardDayOrder.forEach(dayName => {
      const dayData = dayMap.get(dayName);
      if (dayData) {
        orderedDays.push(dayData);
      }
    });

    // If there are any days not in our standard order, add them at the end
    this.weekData.days.forEach((day: DayData) => {
      if (!this.standardDayOrder.includes(day.day)) {
        orderedDays.push(day);
      }
    });

    // Update weekData.days with ordered array
    this.weekData.days = orderedDays;

    // Update component's days property
    this.days = orderedDays;
  }

  setTodayAsDefaultDay(): void {
    if (!this.days || this.days.length === 0) return;

    const today = new Date();
    const dayNames = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const todayName = dayNames[today.getDay()];

    const todayIndex = this.days.findIndex(day => day.day === todayName);
    if (todayIndex !== -1) {
      this.currentDayIndex = todayIndex;
    }

    this.loadCurrentDayTodos();
  }

  loadCurrentDayTodos(): void {
    if (!this.days || this.days.length === 0) {
      this.currentDayTodos = [];
      return;
    }

    const currentDay = this.days[this.currentDayIndex];
    if (currentDay && Array.isArray(currentDay.todos)) {
      this.currentDayTodos = [...currentDay.todos];
      //sort by position
      this.currentDayTodos.sort((a, b) => a.position - b.position);
    } else {
      this.currentDayTodos = [];
    }
  }

  changeDay(direction: number): void {
    const newIndex = this.currentDayIndex + direction;
    if (newIndex >= 0 && newIndex < this.days.length) {
      this.currentDayIndex = newIndex;
      this.loadCurrentDayTodos();
      this.cancelEdit();
    }
  }

  get currentDay(): DayData | null {
    return this.days && this.days.length > 0 ? this.days[this.currentDayIndex] : null;
  }

  updateState(section: string, value: string): void {
    if (section in this.editorState) {
      this.editorState[section] = value;

      if(section === 'fontSize') {
        this.remainingChars = this.fontCharLimits[value]- this.newTodoContent.length; // Reset remaining characters
      }
    }
  }

  isActive(category: string, value: string): boolean {
    return this.editorState[category] === value;
  }

  addTodo(): void {
    if (!this.newTodoContent.trim() || !this.currentDay) return;
    if (this.currentDayTodos.length >= this.maxTodos) return;

    const newTodo: Todo = {
      id: `${Date.now().toString()}-${Math.random().toString(36).substr(2, 9)}`,
      dayId: this.currentDay.dayId,
      content: this.newTodoContent,
      actionApplied: false,
      actionType: null,
      textColor: this.editorState['fontColor'],
      fontSize: this.editorState['fontSize'],
      scratchColor: null,
      position: this.currentDayTodos.length
    };

    this.currentDayTodos = [...this.currentDayTodos, newTodo];
    this.saveTodo.emit(newTodo);
    this.newTodoContent = '';
    this.remainingChars =  this.fontCharLimits[this.editorState['fontSize']]- this.newTodoContent.length; // Reset remaining characters 
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

    const todoIndex = this.currentDayTodos.findIndex(t => t.id === this.editingTodoId);
    if (todoIndex === -1) return;

    const updatedTodo: Todo = {
      ...this.currentDayTodos[todoIndex],
      content: this.newTodoContent,
      textColor: this.editorState['fontColor'],
      fontSize: this.editorState['fontSize']
    };

    // Update local array
    this.currentDayTodos = [
      ...this.currentDayTodos.slice(0, todoIndex),
      updatedTodo,
      ...this.currentDayTodos.slice(todoIndex + 1)
    ];

    // Emit the update
    this.updateTodo.emit(updatedTodo);
    this.cancelEdit();
    this.remainingChars = this.fontCharLimits[this.editorState['fontSize']]- this.newTodoContent.length; // Reset remaining characters
  }

  cancelEdit(): void {
    this.editingTodoId = undefined;
    this.newTodoContent = '';
  }

  deleteTask(todo: Todo, event: Event): void {
    event.stopPropagation();
    this.currentDayTodos = this.currentDayTodos.filter(t => t.id !== todo.id);
    this.deleteTodo.emit(todo);
  }

  getTodoClasses(todo: Todo): any {
    return {
      'todo-item': true,
      [`font-${todo.fontSize.toLowerCase()}`]: true,
      [`color-${todo.textColor}`]: true,
      'action-applied': todo.actionApplied,
      [todo.actionType?.toLowerCase() || '']: todo.actionApplied && todo.actionType
    };
  }

  getStrikeColor(todo: Todo): string {
    if (todo.actionType === 'SCRATCHED') {
      return todo.scratchColor !== null ? todo.scratchColor : 'black';
    }
    return 'green';
  }

  trackById(index: number, todo: Todo): string {
    return todo.id || '';
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
      actionApplied: !todo.actionApplied || todo.actionType === null,
      actionType: (!todo.actionApplied || todo.actionType === null)
        ? this.editorState['actionType'] === 'done' ? 'DONE' : 'SCRATCHED'
        : null,
      scratchColor: this.editorState['actionType'] === 'scratch'
        ? this.editorState['fontColor']
        : null
    };

    this.currentDayTodos = this.currentDayTodos.map(item =>
      item.id === todo.id ? updatedTodo : item
    );

    this.updateTodo.emit(updatedTodo);
  }

 

  enforceCharLimit(): void {
    const fontSize = this.editorState['fontSize'];
    const limit = this.fontCharLimits[fontSize] || 100; // fallback
  
    this.remainingChars = limit - this.newTodoContent.length;
  
    // If typed more than limit, trim it
    if (this.remainingChars < 0) {
      this.newTodoContent = this.newTodoContent.slice(0, limit);
      
    }
  }
  
  

  
}