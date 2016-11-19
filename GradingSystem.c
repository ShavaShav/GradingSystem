#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <math.h>

#define DEBUG 0	// 1 for debug printouts, 0 for production

//FUNCTION PROTOTYPES
void Read();
void CalcDue();
void CalcWeight();
void CumGrade();
void ReCalc();

void Update(int, int);
void Update1(int);
void Update2(int);
void ContUpdate();

void Print();
void PrintCase1();
void PrintCase2();
void PrintEditCase1();
void PrintEditCase2();
void PrintCase3();
void PrintCase4();
void PrintCase5();
void PrintCase6();
void PrintCase7();

void AddRecords(int*);
void AddBulkRecords(int*);
void AddCourses(int*);
void AddBulkCourses(int*);

//GLOBAL VARIABLES
FILE *inputFile;						//inputFile variable - 1st line BLANK!
FILE *coursesFile;						//coursesFile variable

int code[500]; 			// 5 digit course code
char task[500][50]; 	// Task: Assn, Quiz, Test, Exam
int taskNum[500]; 		// Task number
int dueDate[500][3]; 	// 0 = Day, 1 = Month, 2 = Year
float weight[500]; 		// Real, out of 100 -> /100 to get out of 1
float grade[500]; 		// Real, out of 100
int courses_code[500];			// from courses.txt
char courses_name[500][50]; 	// each cell is next course, check file for order.
char courses_major[500];
int courses_sem[500];

int i, k;
char line[] = "------------------------------------------------------------\n";
int numRecords, numCourses; 		//numRecords int updated within Read()
char currentDate[50];

int dueDays[500]; 		//Number of days until due
float weighted_grade[500];	//Weighted grade (grade * weight)
float accum_wtd_grade[50];
float accum_weight[50];
float total_weight[50];
float present_grade[50];
float cum_grade[50] = {0}, mjr_cum_grade[50] = {0};
int current_semester[25] = {0};	// float for summer semesters
int sem_count;

//CONTROL FUNCTION 0000
int main ()
{
	Read();
	CalcDue();
	CalcWeight();
	CumGrade();
	Print();
return 0;
}
//READ FUNCTION
void Read()
{
	inputFile = fopen("./in.txt", "r");		//Opening input.txt for reading into inputFile
	int i = 0; 								//Initializing counter
	if (inputFile == NULL)					//No file found
		printf("Error: Input file cannot be opened.\n");
	else
	{
		while (!feof(inputFile)) 		//Continuous read to end of file
		{
			fscanf(inputFile, "%d\t%s\t%d\t%d-%d-%d\t%f\t%f", &code[i], task[i], &taskNum[i], &dueDate[i][0], &dueDate[i][1], &dueDate[i][2], &weight[i], &grade[i]); //2D arrays dont need &
			i++;			 // Next record and counter
		}// eof scan
		fclose(inputFile); 	// Closing inputFile
		numRecords = i - 1; 	// Copying counter to number of records
	}

	coursesFile = fopen("./courses.txt", "r");	// Opening input.txt for reading into inputFile
	i = 0; 						// Re-Initializing counter
	if (coursesFile == NULL)	// No file found
		printf("Error: Courses file cannot be opened.\n");
	else
	{
		while (!feof(coursesFile)) 	// Continuous read to end of file
		{
			fscanf(coursesFile, "%d\t%s\t%c\t%d\n", &courses_code[i], courses_name[i], &courses_major[i], &courses_sem[i]);
			i++;			 // Next record and counter
		}// eof scan
		fclose(coursesFile); 		// Closing inputFile
		numCourses = i;
	}
}
//	PRINT FUNCTION
void Print()	//	MAIN MENU
{
	int option; // Menu Screen options for what to print
	char main_menu[200];
	strcpy(main_menu, "1 - Records\n2 - Edit Records\n3 - Add Courses\n4 - Grades\n5 - Cumulative Grades\n6 - Upcoming Deadlines\n7 - Exams\n0 - Exit\n\nPlease choose option: ");
	printf("%s***MAIN MENU***\n%s%s", line, line, main_menu);
	scanf("%d", &option);
	while (option != 0)
	{
		printf("\n");
		switch (option)
		{
		case 1 :	PrintCase1();	//RECORDS
			break;
		case 2 :	PrintCase2();	//EDIT RECORDS
			ReCalc();
			break;
		case 3 : 	PrintCase3();	//ADD COURSES
			break;
		case 4 : 	PrintCase4();	//GRADES
			break;
		case 5 : 	PrintCase5();	//CUMULATIVE GRADES
			break;
		case 6 :	PrintCase6();	//DEADLINES
			break;
		case 7 :	PrintCase7();	//EXAMS
			break;
		default: 	printf("Invalid Input\n\n");
			break;
		}
		printf("\n%s***MAIN MENU***\n%s%s", line, line, main_menu);
		scanf("%d", &option);
	}
	printf("\n<(^_^)><(^_^<)(>^_^)><(^_^<)<(^_^)><(^_^<)(>^_^)><(^_^<)<(^_^)>\n");
}
void PrintCase1()	//	RECORDS
{
	printf("%sRECORDS\n%s", line, line);
	printf("Code\tTask #\tDue Date\tWeight\tGrade\tTime Left\n");	// Reprinting Input
	for (i = 0; i < numRecords; i++)
	{
		printf("%d\t%s %d\t%02d-%02d-%d\t%0.2f\t%0.2f\t", code[i], task[i], taskNum[i], dueDate[i][0], dueDate[i][1], dueDate[i][2], weight[i], grade[i]);

		if (dueDays[i] >= 1) 	// Due Date Checker
			printf("%d days left\n", dueDays[i]);
		else if (dueDays[i] >= 0)
			printf("Due Today!\n");
		else
			printf("Past Due\n");
	}// eof records
	printf("\nNumber of Records: %01d\n", numRecords); //Number of Records
}
void PrintCase2()	//	EDIT RECORDS
{
	int option; // Menu Screen options for what to print
	char edit_menu[200];
	strcpy(edit_menu, "1 - Update Records\n2 - Add Records\n0 - Exit\n\nPlease choose option: ");
	printf("%s***EDIT RECORDS***\n%s%s", line, line, edit_menu);
	scanf("%d", &option);
	while (option != 0)
	{
		printf("\n");
		switch (option)
		{
		case 1 :	PrintEditCase1();	//UPDATE RECORDS
			break;
		case 2 :	PrintEditCase2();	//ADD RECORDS
			break;
		default: 	printf("Invalid Input\n\n");
			break;
		}
		printf("%s***EDIT RECORDS***\n%s%s", line, line, edit_menu);
		scanf("%d", &option);
	}
}
void PrintCase3()	//	ADD COURSES
{
	int option; // Menu Screen options for adding courses
	char course_menu[100];
	strcpy(course_menu, "1 - Add Courses with Wizard\n2 - Add Courses Manually\n0 - Exit\n\nPlease choose option: ");
	printf("%sADD COURSES\n%s%s", line, line, course_menu);
	scanf("%d", &option);
	while (option != 0)
	{
		coursesFile = fopen("./courses.txt", "a+");		//Opening courses.txt for reading/appending coursesFile
		if (coursesFile == NULL)					//No file found
			printf("Error: Courses file cannot be opened.\n");
		else
		{
			int addCourses=0;
			printf("\n");
			switch (option)
			{
				case 1 :	AddBulkCourses(&addCourses);	//COURSES WIZARD
					break;
				case 2 :	AddCourses(&addCourses);	//MANUAL COURSES ENTRY
					break;
				default: 	printf("Invalid Input\n\n");
					break;
			}
			printf("\n%s%d Courses Added!\n%s# | Code\tCourse\tMajor\tSemester\n", line, addCourses, line);
			for(i=0; i<addCourses; i++)
				printf("%d | %d\t%s\t%c\t%d\n", i+1, courses_code[numCourses-addCourses+i], courses_name[numCourses-addCourses+i], courses_major[numCourses-addCourses+i], courses_sem[numCourses-addCourses+i]);
		}
		printf("\n%sADD COURSES\n%s%s", line, line, course_menu);
		scanf("%d", &option);
	}
}
void PrintCase4()	//	GRADES
{
	printf("%sSEMESTER %d GRADES\n%s", line, sem_count, line);
	printf("Code\tCourse\t\tPresent\t  Accumulated%s\n\t\t\tGrade\t  Weight%s\n",
											(DEBUG)?"\tTotal":"",		(DEBUG)?"\tWeight":"");
	for (i = 0; i < numCourses; i++)
	{
		if (sem_count == courses_sem[i])
		{
			printf("%d\t%s\t%0.2f\t  %0.2f", courses_code[i], courses_name[i], present_grade[i] * 100, accum_weight[i]);
			if (DEBUG)
				printf("\t\t%0.2f", total_weight[i]);
			puts("");
		}
	}
	int choice;
	printf("%sPREVIOUS GRADES\n%s", line, line);
	printf("Please enter semester, or 0 to exit: ");
	scanf("%d", &choice);
	while (choice != 0)
	{
		if (choice>sem_count || choice<1)
		{
			printf("Semester doesn't exist.\n");
		}
		else 
		{
			printf("%sSEMESTER %d GRADES\n%s", line, choice, line);
			printf("Code\tCourse\t\tPresent\t  Accumulated%s\n\t\t\tGrade\t  Weight%s\n",
											(DEBUG)?"\tTotal":"",		(DEBUG)?"\tWeight":"");
			for (i = 0; i < numCourses; i++)
			{
				if (choice == courses_sem[i])
				{
					printf("%d\t%s\t%0.2f\t  %0.2f", courses_code[i], courses_name[i], present_grade[i] * 100, accum_weight[i]);
					if (DEBUG)
						printf("\t\t%0.2f", total_weight[i]);
			puts("");
				}
			}
		}
		printf("%sPlease enter semester, or 0 to exit: ", line);
		scanf("%d", &choice);
	}
}
void PrintCase5()	//	CUMULATIVE GRADES
{
	float tot_cum_grade = 0; 
	float tot_mjr_cum_grade = 0;
	int gradeknt = 0;
	int mjrgradeknt = 0;
	printf("%sCUMULATIVE GRADES\n%s", line, line);
	for (i = 0; i < sem_count; i++)
	{
		printf("Semester %d\n", current_semester[i]);
		printf("  Cumulative Grade: %0.2f\n  Major Cumulative Grade: %0.2f\n\n", cum_grade[i] * 100, mjr_cum_grade[i] * 100);
		if (cum_grade[i] != 0)
		{
			tot_cum_grade += cum_grade[i];
			gradeknt++;
		}
		if (mjr_cum_grade[i] !=0)
		{
			tot_mjr_cum_grade += mjr_cum_grade[i];
			mjrgradeknt++;
		}
	}
	printf("Program\n");
	printf("  Cumulative Grade: %0.2f\n  Major Cumulative Grade: %0.2f\n", tot_cum_grade / gradeknt * 100, tot_mjr_cum_grade / mjrgradeknt * 100);
}
void PrintCase6()	//	DEADLINES
{
	// Sorting deadlines of all records in order, very convuluted but works
	int dueDays_sorted[numRecords];
	int code_srt[numRecords];
	char coursename_srt[numRecords][25];
	char task_srt[numRecords][25];
	int taskNum_srt[numRecords];
	int dueDate_srt[numRecords][3]; 
	float weight_srt[numRecords];
	for(i = 0; i < numRecords; i++) //copying records for use in function
	{
		dueDays_sorted[i] = dueDays[i];
		code_srt[i] = code[i];
		strcpy(task_srt[i], task[i]);
		taskNum_srt[i] = taskNum[i];
		dueDate_srt[i][0] = dueDate[i][0]; 
		dueDate_srt[i][1] = dueDate[i][1]; 
		dueDate_srt[i][2] = dueDate[i][2]; 
		weight_srt[i] = weight[i];
		for(k = 0; k < numCourses; k++) //pulling course name based on code array
		{
			if (courses_code[k] == code[i])
				strcpy(coursename_srt[i], courses_name[k]);
		}
	}
	int swap;
	float swapweight;
	char swaptask[25];
	for (i = 0; i < (numRecords - 1); i++) // "Bubble Sort" begins
	{
		for (k = 0; k < (numRecords - i - 1); k++)
		{
			if (dueDays_sorted[k] > dueDays_sorted[k+1])
			{
				swap = dueDays_sorted[k];
				dueDays_sorted[k] = dueDays_sorted[k+1];
				dueDays_sorted[k+1] = swap;
				swap = code_srt[k];
				code_srt[k] = code_srt[k+1];
				code_srt[k+1] = swap;
				strcpy(swaptask, task_srt[k]);
				strcpy(task_srt[k], task_srt[k+1]);
				strcpy(task_srt[k+1], swaptask);
				swap = taskNum_srt[k];
				taskNum_srt[k] = taskNum_srt[k+1];
				taskNum_srt[k+1] = swap;
				swap = dueDate_srt[k][0];
				dueDate_srt[k][0] = dueDate_srt[k+1][0];
				dueDate_srt[k+1][0] = swap;
				swap = dueDate_srt[k][1];
				dueDate_srt[k][1] = dueDate_srt[k+1][1];
				dueDate_srt[k+1][1] = swap;
				swap = dueDate_srt[k][2];
				dueDate_srt[k][2] = dueDate_srt[k+1][2];
				dueDate_srt[k+1][2] = swap;
				swapweight = weight_srt[k];
				weight_srt[k] = weight_srt[k+1];
				weight_srt[k+1] = swapweight;
				strcpy(swaptask, coursename_srt[k]);
				strcpy(coursename_srt[k], coursename_srt[k+1]);
				strcpy(coursename_srt[k+1], swaptask);
			}
		}
	}	//end sort
	printf("%sUPCOMING DEADLINES\t Today is %s\n%s", line, currentDate, line);
	printf("Code\tName\t\tTask #\tDue Date\tWeight\tTime Left\n");	// Reprinting Input
	for (i = 0; i < numRecords; i++)
	{
		if (dueDays_sorted[i] >= 0)		// Today
			if (dueDays_sorted[i] >= 1) 	// Today-3Wks, print deadline
				if (dueDays_sorted[i] >= 21)	// >3Wks, skip
					continue;
				else
				{
					printf("%d\t%s\t%s %d\t%02d-%02d-%d\t%0.2f\t", code_srt[i], coursename_srt[i], task_srt[i], taskNum_srt[i], dueDate_srt[i][0], dueDate_srt[i][1], dueDate_srt[i][2], weight_srt[i]);
					printf("%d days\n", dueDays_sorted[i]);
				}
			else
			{
				printf("%d\t%s\t%s %d\t%02d-%02d-%d\t%0.2f\t", code_srt[i], coursename_srt[i], task_srt[i], taskNum_srt[i], dueDate_srt[i][0], dueDate_srt[i][1], dueDate_srt[i][2], weight_srt[i]);
				printf("Due Today!\n");
			}
	}// eof records
}
void PrintCase7()	//	EXAMS
{
	float passing_grade;
	printf("%sSEMESTER %d EXAMS\n%s", line, sem_count, line);
	printf("Code\tCourse\t\tPresent\t  Exam\t  Exam\t\tGrade Needed\n\t\t\tGrade\t  Weight  Date\t\tto Pass\n");
	for (i=0; i < numRecords; i++)
	{	
		for (k = 0; k < numCourses; k++)
		{
			if (strcmp(task[i], "Exam") == 0 && courses_code [k] == code[i] && sem_count == courses_sem[k])
			{
			passing_grade = ((1-(weight[i]/100))-((1-(weight[i]/100))*present_grade[k]))/(weight[i]/100);	//Calculates passing grade
			printf("%d\t%s\t%0.2f\t  %0.2f\t  %02d-%02d-%d\t%0.2f\n", code[i], courses_name[k], present_grade[k] * 100, weight[i], dueDate[i][0], dueDate[i][1], dueDate[i][2], passing_grade * 100);
			}
		}
	}
}

// FROM PrintCase2 (EDIT RECORDS)
void PrintEditCase1()	//	UPDATE RECORDS
{
	int record, attribute;
	char update_menu[200];
	strcpy(update_menu, "Please choose record to update (#) or press 0 to exit to menu: ");
	printf("%sUPDATE RECORDS\n%s# | Code\tTask #\tDue Date\tGrade\n", line, line);// Reprinting Input
	for (i = 0; i < numRecords; i++)
	{
		printf("%d | %d\t%s %d\t%02d-%02d-%d\t%0.2f\n", i + 1, code[i], task[i], taskNum[i], dueDate[i][0], dueDate[i][1], dueDate[i][2], grade[i]);
	}// eof records
	printf("\n***Number of Records: %01d***\n%s\n", numRecords, line); //Number of Records

	printf("%s", update_menu);
	scanf("%d", &record);
	while (record != 0)
	{
		i = record;
		i--;
		char header[100];
		strcpy(header, "\n#  | Code\tTask #\tDue Date\tGrade\n");
		printf("%s%d | %d\t%s %d\t%02d-%02d-%d\t%0.2f\n%s", header, record, code[i], task[i], taskNum[i], dueDate[i][0], dueDate[i][1], dueDate[i][2], grade[i], line);
		printf("Please choose which attribute to update:\n1 - Due Date\n2 - Grade\n\n");
		scanf("%d", &attribute);
		Update(i, attribute);

		printf("%sUPDATE RECORDS\n%s%s%s", line, line, header, line);// Reprinting Input
			for (i = 0; i < numRecords; i++)
			{
				printf("%d | %d\t%s %d\t%02d-%02d-%d\t%0.2f\n", i + 1, code[i], task[i], taskNum[i], dueDate[i][0], dueDate[i][1], dueDate[i][2], grade[i]);
			}// eof records
		printf("Record %d Updated!\n", record);
		printf("%s%d | %d\t%s %d\t%02d-%02d-%d\t%0.2f\n%s\n", header, record, code[record - 1], task[record - 1], taskNum[record - 1], dueDate[record - 1][0], dueDate[record - 1][1], dueDate[record - 1][2], grade[record - 1], line);
		printf("%s", update_menu);
		scanf("%d", &record);
	}
}
void PrintEditCase2()	//	ADD RECORDS
{
	int option; // Menu Screen options for adding records
	char record_menu[100];
	strcpy(record_menu, "1 - Add Records with Wizard\n2 - Add Records Manually\n0 - Exit\n\nPlease choose option: ");
	printf("%sADD RECORDS\n%s%s", line, line, record_menu);
	scanf("%d", &option);
	while (option != 0)
	{
		inputFile = fopen("./in.txt", "a+");		//Opening input.txt for reading/appending inputFile
		if (inputFile == NULL)					//No file found
			printf("Error: Input file cannot be opened.\n");
		else
		{
			int addRecords=0;
			printf("\n");
			switch (option)
			{
				case 1 :	AddBulkRecords(&addRecords);	//RECORDS WIZARD
					break;
				case 2 :	AddRecords(&addRecords);	//MANUAL RECORDS ENTRY
					break;
				default: 	printf("Invalid Input\n\n");
					break;
			}
			printf("\n%s%d Records Added!\n%s#  | Code\tTask\t#\tDue Date\tWeight\tGrade\n", line, addRecords, line);
			for(i=0; i<addRecords; i++)
				printf("%d | %d\t%s\t%d\t%02d-%02d-%d\t%0.2f\t%0.2f\n", i+1, code[numRecords-addRecords+i], task[numRecords-addRecords+i], taskNum[numRecords-addRecords+i], dueDate[numRecords-addRecords+i][0], dueDate[numRecords-addRecords+i][1], dueDate[numRecords-addRecords+i][2], weight[numRecords-addRecords+i], grade[numRecords-addRecords+i]);
		}
		printf("\n%sADD RECORDS\n%s%s", line, line, record_menu);
		scanf("%d", &option);
	}
}
// FROM PrintEditCase1 (UPDATE RECORDS)
void Update(int record, int attribute)	//	UPDATE
{
	inputFile = fopen("./in.txt", "r+");		//Opening input.txt for updating inputFile
	if (inputFile == NULL)					//No file found
		printf("Error: Input file cannot be opened for updating.\n");
	else
	{
		switch (attribute)
		{
		case 1 :	Update1(record);
			break;
		case 2 :	Update2(record);
			break;
		default :	printf("Invalid option.\n");
			break;
		}
	}
	fclose(inputFile); 	// Closing inputFile
}
void Update1(int record) 				//	UPDATING DUE DATE
{
	printf("\nCurrent Due Date: %02d-%02d-%d\n", dueDate[record][0], dueDate[record][1], dueDate[record][2]);
	printf("New Due Date (format DD-MM-YYYY): ");
	scanf("%d-%d-%d", &dueDate[record][0], &dueDate[record][1], &dueDate[record][2]);
	printf("\n");
	ContUpdate();
}
void Update2(int record) 				//	UPDATING GRADE
{
	printf("\nCurrent Grade: %0.2f\n", grade[record]);
	printf("New Grade: ");
	scanf("%f", &grade[record]);
	printf("\n");
	ContUpdate();
}
void ContUpdate()						//	REPRINTING INPUT FILE WITH UPDATE
{
	for (i = 0; i < numRecords; i++) 		//Continuous update to end of file
	{
		fprintf(inputFile, "\n%d\t%s\t%d\t%02d-%02d-%d\t%0.2f\t%0.2f", code[i], task[i], taskNum[i], dueDate[i][0], dueDate[i][1], dueDate[i][2], weight[i], grade[i]);
	}
}

// FROM PrintEditCase2 (ADDING RECORDS)
void AddBulkRecords(int *addRecords)	//RECORD WIZARD
{
	int choice, choice2;
	printf("%sRECORD WIZARD\n%s# | Code\t Course\n", line, line);
	for (i=0; i<numCourses; i++)
	{
		printf("%d | %d\t %s\n", i+1, courses_code[i], courses_name[i]);
	}
	printf("\nWhat course will you be adding record(s) to? (#): ");
	scanf("%d", &choice);
	printf("\nHow many records would you like to add?: ");
	scanf("%d", addRecords);
	int addCode, addTaskNum[*addRecords], addDay[*addRecords], addMonth[*addRecords], addYear[*addRecords], i=0;
	float addWeight[*addRecords];
	char addTask[*addRecords][50];
	for (i=0; i<*addRecords; i++)
	{	printf("\n%sRECORD %d\n", line, i+1);
		fprintf(inputFile, "%d\t", courses_code[choice-1]);
		printf("\n1 - Assignments\t\t4 - Quiz\n2 - Lab\t\t\t5 - Test\n3 - Project\t\t6 - Exam\n\nChoose Task: ");
		scanf("%d", &choice2);
		switch (choice2)
		{
			case 1:		strcpy(addTask[i], "Assn");
				break;
			case 2:		strcpy(addTask[i], "Lab.");
				break;
			case 3:		strcpy(addTask[i], "Proj");
				break;
			case 4:		strcpy(addTask[i], "Quiz");
				break;
			case 5:		strcpy(addTask[i], "Test");
				break;
			case 6:		strcpy(addTask[i], "Exam");
				break;
			default: 	printf("Incorrect value.\n");
				break;
		}
		fprintf(inputFile, "%s\t", addTask[i]);
		printf("Enter %s number: ", addTask[i]);
		scanf("%d", &addTaskNum[i]);
		fprintf(inputFile, "%d\t", addTaskNum[i]);
		printf("\nEnter Date (DD-MM-YYYY): ");
		scanf("%d-%d-%d", &addDay[i], &addMonth[i], &addYear[i]);
		fprintf(inputFile, "%02d-%02d-%d\t", addDay[i], addMonth[i], addYear[i]);
		printf("\nEnter Weight: ");
		scanf("%f", &addWeight[i]);
		fprintf(inputFile, "%0.2f\t", addWeight[i]);
		fprintf(inputFile, "%0.2f\n", 0.00);
	}
	rewind(inputFile);				//Bring Cursor back to beginning of file
	while (!feof(inputFile)) 		//Continuous read to end of file
	{
		fscanf(inputFile, "\n%d\t%s\t%d\t%d-%d-%d\t%f\t%f", &code[i], task[i], &taskNum[i], &dueDate[i][0], &dueDate[i][1], &dueDate[i][2], &weight[i], &grade[i]); //2D arrays dont need &
		i++;			 // Next record and counter
	}// eof scan
	fclose(inputFile); 	// Closing inputFile
	numRecords = i - 1; 	// Copying counter to number of records
}	
void AddRecords(int *addRecords)		//MANUAL RECORD ENTRY
{
	printf("%sADD RECORDS MANUALLY\n%s", line, line);
	printf("\nHow many records would you like to add?: ");
	scanf("%d", addRecords);
	int addCode[*addRecords], addTaskNum[*addRecords], addDay[*addRecords], addMonth[*addRecords], addYear[*addRecords], i=0;
	float addWeight[*addRecords];
	char addTask[*addRecords][50];
	if (*addRecords>0)
	{
		printf("\n%sEnter records in this format:\n\n\t  Code Task # Date Weight\ne.g.\t  60125 Assn 1 12-03-2016 25\n\n", line);
		for (i=0; i<*addRecords; i++)
		{
			printf("Record %d: ", i+1);
			scanf("%d\t%s\t%d\t%d-%d-%d\t%f", &addCode[i], addTask[i], &addTaskNum[i],&addDay[i], &addMonth[i], &addYear[i], &addWeight[i]);
			fprintf(inputFile, "%d\t%s\t%d\t%02d-%02d-%d\t%0.2f\t%0.2f\n", addCode[i], addTask[i], addTaskNum[i], addDay[i], addMonth[i], addYear[i], addWeight[i], 0.00);
		}
		rewind(inputFile);				//Bring Cursor back to beginning of file
		while (!feof(inputFile)) 		//Continuous re-read to end of file
		{
			fscanf(inputFile, "\n%d\t%s\t%d\t%d-%d-%d\t%f\t%f", &code[i], task[i], &taskNum[i], &dueDate[i][0], &dueDate[i][1], &dueDate[i][2], &weight[i], &grade[i]); //2D arrays dont need &
			i++;			 // Next record and counter
		}// eof re-scan
		fclose(inputFile); 	// Closing inputFile
		numRecords = i - 1; 	// Copying counter to number of records
	}	
}

// FROM PrintCase3 (ADDING COURSES)
void AddBulkCourses(int *addCourses)	//COURSE WIZARD
{
	printf("%sCOURSE WIZARD\n%s# | Code\tCourse\t\tMajor\tSemester\n", line, line);
	for (i=0; i<numCourses; i++)
	{
		printf("%d | %d\t%s\t%c\t%d\n", i+1, courses_code[i], courses_name[i], courses_major[i], courses_sem[i]);
	}
	printf("\nHow many courses would you like to add?: ");
	scanf("%d", addCourses);
	int addCode[*addCourses], addSem[*addCourses], i=0;
	char addName[*addCourses][50];
	char addMajor[*addCourses];
	for (i=0; i<*addCourses; i++)
	{	
		printf("\n%sCourse %d\n", line, i+1);
		printf("\nEnter Course Code (6 digits): ");
		scanf("%d", &addCode[i]);
		fprintf(coursesFile, "%d\t", addCode[i]);
		printf("Enter %d Name (string): ", addCode[i]);
		scanf("%s", addName[i]);
		fprintf(coursesFile, "%s\t", addName[i]);
		printf("\nIs %d - %s a Major course? (Y or N): ", addCode[i], addName[i]);
		scanf("%s", &addMajor[i]);
		fprintf(coursesFile, "%c\t", addMajor[i]);
		printf("\nWhat semester are you taking %d - %s? (#): ", addCode[i], addName[i]);
		scanf("%d", &addSem[i]);
		fprintf(coursesFile, "%d\n", addSem[i]);
	}
	rewind(coursesFile);	//Bring Cursor back to beginning of file
	i = 0;				
	while (!feof(coursesFile)) 		//Continuous read to end of file
	{
		fscanf(coursesFile, "%d\t%s\t%c\t%d\n", &courses_code[i], courses_name[i], &courses_major[i], &courses_sem[i]);
		i++;			 // Next course and counter
	}// eof scan
	fclose(coursesFile); 	// Closing coursesFile
	numCourses = i; 	// Copying counter to number of course
}	
void AddCourses(int *addCourses)		//MANUAL RECORD ENTRY
{
	printf("%sADD COURSES MANUALLY\n%s# | Code\tCourse\tMajor\tSemester\n", line, line);
	for (i=0; i<numCourses; i++)
	{
		printf("%d | %d\t%s\t%c\t%d\n", i+1, courses_code[i], courses_name[i], courses_major[i], courses_sem[i]);
	}
	printf("\nHow many courses would you like to add?: ");
	scanf("%d", addCourses);
	int addCode[*addCourses], addSem[*addCourses], i=0;
	char addName[*addCourses][50];
	char addMajor[*addCourses];
	if (*addCourses>0)
	{
		printf("\n%sEnter Courses in this format:\n\n\t  Code Course Major Semester\ne.g.\t  60125 Algebra Y 1\n\n", line);
		for (i=0; i<*addCourses; i++)
		{
			printf("Course %d: ", i+1);
			scanf("%d\t%s\t%c\t%d", &courses_code[i], courses_name[i], &courses_major[i], &courses_sem[i]);
			fprintf(coursesFile, "%d\t%s\t%c\t%d\n", courses_code[i], courses_name[i], courses_major[i], courses_sem[i]);
		}
		rewind(coursesFile);				//Bring Cursor back to beginning of file
		i = 0;
		while (!feof(coursesFile)) 		//Continuous re-read to end of file
		{
			fscanf(coursesFile, "%d\t%s\t%c\t%d\n", &courses_code[i], courses_name[i], &courses_major[i], &courses_sem[i]);
			i++;			 // Next record and counter
		}// eof re-scan
		fclose(coursesFile); 	// Closing coursesFile
		numCourses = i; 	// Copying counter to number of courses
	}	
}

// CALCULATIONS
void ReCalc()		//RECALCULATING BELOW FUNCTIONS AFTER RECORD CHANGES
{
	CalcDue();
	CalcWeight();
	CumGrade();
}
void CalcDue()		//DUE DEADLINE
{
	//char* dueDate;
	//char* currentDate;
	for (i = 0; i < numRecords; i++)
	{
		struct tm t;

		time_t dueEpoch;
		t.tm_year = dueDate[i][2] - 1900;		// Year since 1990
		t.tm_mon = dueDate[i][1] - 1;         // Month, 0 = jan
		t.tm_mday = dueDate[i][0];          // Day of the month
		t.tm_hour = 23;     			//Default deadline set to noon
		t.tm_min = 59;
		t.tm_sec = 59;
		t.tm_isdst = -1;        // Is DST on? 1 = yes, 0 = no, -1 = unknown
		dueEpoch = mktime(&t); 			// epoch time, when task due
		//dueDate = ctime(&dueEpoch);	// converting DUE to string withinth Normal Date Format

		time_t currentEpoch = time(NULL); 		//epoch time, currently
		strcpy(currentDate, (ctime(&currentEpoch))); 	//converting CURRENT to string with Normal Date Format

		dueDays[i] = (dueEpoch - currentEpoch) / 86400; 	//Task due in __ days
	}
}
void CalcWeight()	//GRADE WEIGHTING
{
	for (k = 0; k < numCourses; k++)
	{
		accum_wtd_grade[k] = 0;
		accum_weight[k] = 0;
		total_weight[k] = 0;
		for (i = 0; i < numRecords; i++)
		{
			weighted_grade[i] = (grade[i] / 100) * (weight[i] / 100); // Calculating weighted grade for each task

			if (code[i] == courses_code[k]) // Finding total weight for each course
			{
				total_weight[k] += weight[i];
				if (grade[i] != 0) // Adding graded weight up for each course
				{
					accum_wtd_grade[k] += weighted_grade[i];
					accum_weight[k] += weight[i];
				}
			}
		}
		if (accum_weight[k] != 0)
			present_grade[k] = accum_wtd_grade[k] * 100 / accum_weight[k];
		else
			present_grade[k] = 0;
	}
}
void CumGrade()		//CUMULATIVE GRADE
{
	int max = 0;
	for (k = 0; k < numCourses; k++)
	{
		if (courses_sem[k]>max)
			max=courses_sem[k];
	}
	sem_count = max;
	for (k = 0; k < sem_count; k++)		// SEMESTER SEARCH (k < sem_count)
	{
		int knt = 0;
		float sum = 0;
		float mjrsum = 0;
		int numCourses_sem = 0;
		current_semester[k] = k+1;
		for (i = 0; i < numCourses; i++)	//COURSES SEARCH
		{
			//	Checks if semester # from courses.tit = k
			if (courses_sem[i] == k+1)
			{
				sum += present_grade[i];
				numCourses_sem+=1;
				if (present_grade[i] == 0)
				{
					numCourses_sem-=1;
				}
				if (courses_major[i] == 'Y' && present_grade[i] != 0)
				{
					mjrsum += present_grade[i];
					knt++;
				}
			}	
		}
		if (sum != 0)
		{
			cum_grade[k] = sum / (numCourses_sem);
			mjr_cum_grade[k] = mjrsum / knt;
		}
	}
}